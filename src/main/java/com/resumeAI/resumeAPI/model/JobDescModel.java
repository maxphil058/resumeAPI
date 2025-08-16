package com.resumeAI.resumeAPI.model;

import jakarta.validation.constraints.NotBlank;

public class JobDescModel {

	@NotBlank(message = "The job description is empty!")
	private String jobDescription;

	@NotBlank(message = "The resume is empty!")
	private String resumeText;

	public JobDescModel(String jobDescription, String resumeText) {
		this.jobDescription = jobDescription;
		this.resumeText = resumeText;
	}

	public String getResumeText() {
		return resumeText;
	}

	public void setResumeText(String resumeText) {
		this.resumeText = resumeText;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
}
