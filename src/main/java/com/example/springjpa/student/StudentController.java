package com.example.springjpa.student;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class StudentController {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/students")
	public List<Student> retreiveStudents()
	{
		return studentRepository.findAll();
		
	}
	
	@GetMapping("/student/{id}")
	public Student retreiveStudentById(@PathVariable long id) throws StudentNotFoundException
	{
		Optional<Student> student = studentRepository.findById(id);
		
		if(!student.isPresent())
			throw new StudentNotFoundException("id - " + id);
		
		return student.get();
	}
	

	@DeleteMapping("/deletestudent/{id}")
	public void deleteStudent(@PathVariable long id)
	{
		studentRepository.deleteById(id);
	}
	
	@PostMapping("/createstudent")
	public ResponseEntity<Object> createStudent(@RequestBody Student student)
	{
		Student savedStudent = studentRepository.save(student);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedStudent.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/updatestudent/{id}")
	public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable long id) {

		Optional<Student> studentOptional = studentRepository.findById(id);

		if (!studentOptional.isPresent())
			return ResponseEntity.notFound().build();

		student.setId(id);
		
		studentRepository.save(student);

		return ResponseEntity.noContent().build();
	}
}
