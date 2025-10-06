package com.example.Bajaj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface Websql1 extends JpaRepository<Employee, Long> {

    @Query(value = """
                SELECT e1.EMP_ID AS emp_id,
                       e1.FIRST_NAME AS first_name,
                       e1.LAST_NAME AS last_name,
                       d.DEPARTMENT_NAME AS department_name,
                       COUNT(e2.EMP_ID) AS younger_employees_count
                FROM EMPLOYEE e1
                JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
                LEFT JOIN EMPLOYEE e2
                    ON e1.DEPARTMENT = e2.DEPARTMENT
                    AND (YEAR(CURRENT_DATE) - YEAR(e2.DOB)) < (YEAR(CURRENT_DATE) - YEAR(e1.DOB))
                GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
                ORDER BY e1.EMP_ID DESC
            """, nativeQuery = true)
    List<Object[]> findYoungerEmployeesCountByDepartment();
}
