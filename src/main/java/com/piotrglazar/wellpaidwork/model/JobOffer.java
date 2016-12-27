package com.piotrglazar.wellpaidwork.model;

public class JobOffer {

    private final String id;
    private final String name;
    private final String city;
    private final String category;
    private final String title;
    private final String level;
    private final Salary salary;
    private final EmploymentType employmentType;

    public JobOffer(String id, String name, String city, String category, String title, String level, Salary salary,
                    EmploymentType employmentType) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.category = category;
        this.title = title;
        this.level = level;
        this.salary = salary;
        this.employmentType = employmentType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public Salary getSalary() {
        return salary;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }
}
