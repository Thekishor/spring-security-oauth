package com.springSecurityClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class TestController {

    @Autowired
    private WebClient webClient;

     record Employee(int id, String name, String address, String email){
    }

    List<Employee> employees = List.of(
            new Employee(100, "Kishor", "Kathmandu", "kishor33@gmail.com"),
            new Employee(101, "Anita", "Pokhara", "anita55@gmail.com"),
            new Employee(102, "Ramesh", "Kathmandu", "ramesh55@gmail.com"),
            new Employee(103, "Sita", "Bhaktapur", "sita21@gmail.com"),
            new Employee(104, "Binod", "Butwal", "binod12@gmail.com"),
            new Employee(105, "Binod", "Pokhara", "binod88@gmail.com"),
            new Employee(106, "Radha", "Pokhara", "radha22@gmail.com"),
            new Employee(107, "Gopal", "Kathmandu", "gopal22@gmail.com"),
            new Employee(108, "Hari", "Butwal", "hari33@gmail.com"),
            new Employee(109, "Laxmi", "Kathmandu", "laxmi12@gmail.com"),
            new Employee(110, "Nabin", "Bhaktapur", "nabin21@gmail.com")
    );

    Map<String, List<Employee>> collect = employees
            .stream()
            .collect(Collectors.groupingBy(employee -> employee.address));


    @GetMapping("/api/employee")
    public Map<String, List<Employee>> employee(){
        return collect;
    }

    @GetMapping("/api/hello")
    public String hello(Principal principal){
        return "Kishor Pandey with Email: "+principal.getName()+", " +
                "Spring boot backend developer with Docker, " +
                "ci/cd jenkins and " +
                "basic cloud deployment knowledge with local stack";
    }
}
