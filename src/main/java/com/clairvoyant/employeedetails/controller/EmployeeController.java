package com.clairvoyant.employeedetails.controller;

import com.clairvoyant.employeedetails.exception.ResourceNotFoundException;
import com.clairvoyant.employeedetails.model.Employee;
import com.clairvoyant.employeedetails.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }


    @GetMapping("/employee/{id}")
    public ResponseEntity< Employee > getEmployeeById(@PathVariable(value = "id") Long empId) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id ::" + empId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity< Employee > updateEmployee(@PathVariable (value = "id") Long empId,
                                             @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException{
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id ::" + empId));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmp_id(employeeDetails.getEmp_id());
        employee.setDateOfBirth(employeeDetails.getDateOfBirth());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setDesignation(employeeDetails.getDesignation());

        final Employee updatedEmp = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmp);
    }

    @DeleteMapping("/employee/{id}")
    public Map< String, Boolean > deleteEmployee(@PathVariable (value = "id") Long empId) throws ResourceNotFoundException{
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id ::" + empId));

        employeeRepository.delete(employee);
        Map< String, Boolean > response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @PatchMapping("/employee/{id}")
    public ResponseEntity<Employee> patchEmployee(@PathVariable (value = "id") Long empId,@RequestParam (value = "dep") String department,@RequestParam (value = "des")String designation) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id ::" + empId));
        employee.setDepartment(department);
        employee.setDesignation(designation);
        final Employee updatedEmp = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmp);
    }



}
