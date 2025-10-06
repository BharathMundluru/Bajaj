package com.example.Bajaj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface Websql extends JpaRepository<Payment, Long> {

    @Query(value = """
        SELECT 
            CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS full_name,
            (YEAR(CURRENT_DATE) - YEAR(e.DOB)) AS age,
            d.DEPARTMENT_NAME AS department_name,
            p.AMOUNT AS highest_salary
        FROM PAYMENTS p
        JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
        JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
        WHERE DAY(p.PAYMENT_TIME) <> 1
        ORDER BY p.AMOUNT DESC
        LIMIT 1
    """, nativeQuery = true)
    Map<String, Object> findHighestSalaryExcludingFirstDay();
}
