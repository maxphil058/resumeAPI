package com.resumeAI.resumeAPI.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.resumeAI.resumeAPI.model.OpenaiResponse;

import reactor.core.publisher.Mono;

@Service
public class ResumeAIService {

	private final WebClient webClient;

	private final WebClient latexWebClient;

	public ResumeAIService(WebClient webClient, @Qualifier("latexWebClient") WebClient latexWebClient) {
		this.webClient = webClient;
		this.latexWebClient = latexWebClient;
	}

//	
	public Mono<ByteArrayResource> generatePDFTEST(String latexCode) {

		return latexWebClient.get().uri("/compile?text={latex}", latexCode) // avoids {…} parsing in values
				.accept(MediaType.APPLICATION_PDF).retrieve()
				.onStatus(HttpStatusCode::isError,
						r -> r.bodyToMono(String.class)
								.flatMap(body -> Mono.error(new ResponseStatusException(r.statusCode(), body))))
				.bodyToMono(byte[].class).map(ByteArrayResource::new);
	}

	public Mono<String> sendToAi(String jobDescription, String resumeText) {

		String template = """
				Tailor the resume specifically to the following job description (JD):
				{{JD}}

				You are a top-tier technical resume writer who follows best practices from Harvard Career Services, The Muse, HBR, Google’s Resume Guidelines, and the book “What Color Is Your Parachute?”. You write resumes that are clean, ATS-optimized, minimal, and tailored for the modern tech industry.

				Your rewriting rules are MANDATORY, not optional:

				1. For EACH role in the EXPERIENCE section:
				   - Write EXACTLY 4 bullet points. No more, no less.
				   - Every bullet MUST:
				       • Start with a strong action verb.
				       • Include at least ONE measurable outcome (number, %, $, time saved, quantity handled, performance metric).
				       • Match a skill, responsibility, or keyword from the provided JD.
				       • Use realistic, plausible estimates if no metrics are given in the original resume.
				   - Rephrase responsibilities from the JD as plausible past achievements.
				   - Integrate tools, frameworks, and processes mentioned in the JD whenever possible.

				2. For EACH project in the PROJECTS section:
				   - Write EXACTLY 4 bullet points.
				   - Follow the same measurable + JD-matching rules as above.

				3. Do NOT copy text from the JD directly — rephrase naturally.

				Special Rule for Certifications Section:
				- If the Certifications section has **no content**, omit it entirely.
				- If there are relevant online courses, trainings, awards, or in-progress certifications, replace the section title with:
				  **Professional Development** — for relevant training/learning
				  **Awards & Achievements** — for competitions, honors, and awards
				  **Relevant Coursework** — for key academic classes
				- Always ensure that if this section exists, it contains 2–4 bullet points of value.


				Output the resume as **plain text only** (no LaTeX), exactly matching the section order and formatting of Jake’s Resume Template:

				________________________________________________________________________________


				FULL NAME
				City, State | email@example.com | (123) 456-7890 | LinkedIn: linkedin.com/in/username | GitHub: github.com/username | Portfolio: yourportfolio.com

				________________________________________________________________________________


				EDUCATION
				University Name – City, State
				Bachelor of Science in [Degree], Minor in [Minor] | Month Year – Month Year
				- Relevant Coursework: [Course 1], [Course 2], [Course 3]

				________________________________________________________________________________


				TECHNICAL SKILLS
				Languages: [List]
				Frameworks & Libraries: [List]
				Tools & Platforms: [List]
				Databases: [List]

				________________________________________________________________________________


				EXPERIENCE
				Job Title – Company Name, City, State | Month Year – Month Year
				- [Bullet 1]
				- [Bullet 2]
				- [Bullet 3]
				- [Bullet 4]

				Job Title – Company Name, City, State | Month Year – Month Year
				- [Bullet 1]
				- [Bullet 2]
				- [Bullet 3]
				- [Bullet 4]

				Job Title – Company Name, City, State | Month Year – Month Year
				- [Bullet 1]
				- [Bullet 2]
				- [Bullet 3]
				- [Bullet 4]

				________________________________________________________________________________


				PROJECTS
				Project Name | Tools/Technologies Used | Month Year – Month Year
				- [Bullet 1]
				- [Bullet 2]
				- [Bullet 3]
				- [Bullet 4]

				Project Name | Tools/Technologies Used | Month Year – Month Year
				- [Bullet 1]
				- [Bullet 2]
				- [Bullet 3]
				- [Bullet 4]

				Project Name | Tools/Technologies Used | Month Year – Month Year
				- [Bullet 1]
				- [Bullet 2]
				- [Bullet 3]
				- [Bullet 4]

				________________________________________________________________________________


				CERTIFICATIONS
				- Certification Name – Issuing Organization | Month Year
				- Certification Name – Issuing Organization | Month Year

				---

				LEADERSHIP & ACTIVITIES
				- Role – Organization Name | Month Year – Month Year
				- Role – Organization Name | Month Year – Month Year

				________________________________________________________________________________


				Here is the original resume to rewrite:
				{{RES}}
				""";

		String finalPrompt = template.replace("{{JD}}", jobDescription).replace("{{RES}}", resumeText);

		Map<String, Object> body = Map.of("model", "gpt-3.5-turbo", "messages",
				List.of(Map.of("role", "user", "content", finalPrompt)));

		return webClient.post().uri("/chat/completions").contentType(MediaType.APPLICATION_JSON).bodyValue(body)
				.retrieve().bodyToMono(OpenaiResponse.class).map(r -> r.getChoices().get(0).getMessage().getContent());
	}

}
