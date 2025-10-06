package com.example.Bajaj;

import jakarta.persistence.*;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ID")
    private Long empId;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "DOB")
    private java.sql.Date dob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT", referencedColumnName = "DEPARTMENT_ID")
    private Department department;

    public Long getId() { return empId; }
    public void setId(Long id) { this.empId = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }


    public java.sql.Date getDob() { return dob; }
    public void setDob(java.sql.Date dob) { this.dob = dob; }
}
