package com.resumeAI.resumeAPI.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumeAI.resumeAPI.model.JobDescModel;
import com.resumeAI.resumeAPI.service.ResumeAIService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeAPIController {

	private final ResumeAIService service;

	public ResumeAPIController(final ResumeAIService service) {
		this.service = service;
	}

	@PostMapping(value = "jobDesc")
	public Mono<String> jobDescriptionUpload(@Valid @RequestBody JobDescModel jobDesc) {

		return service.sendToAi(jobDesc.getJobDescription(), jobDesc.getResumeText());

	}

}
