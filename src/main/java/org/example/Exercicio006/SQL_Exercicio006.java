package org.example.Exercicio006;

public class SQL_Exercicio006 {

    -- a. Returns the names of all Salesperson that don't have any order with Samsonic.
    SELECT s.Name
    FROM Salesperson s
    WHERE s.ID NOT IN (
            SELECT DISTINCT o.salesperson_id
                    FROM Orders o
                    INNER JOIN Customer c ON o.customer_id = c.ID
                    WHERE c.Name = 'Samsonic'
    );

-- Alternativa usando LEFT JOIN (mais eficiente em alguns casos):
    SELECT DISTINCT s.Name
    FROM Salesperson s
    LEFT JOIN Orders o ON s.ID = o.salesperson_id
    LEFT JOIN Customer c ON o.customer_id = c.ID AND c.Name = 'Samsonic'
    WHERE c.ID IS NULL OR o.ID IS NULL;

-- b. Updates the names of Salesperson that have 2 or more orders.
-- It's necessary to add an '*' in the end of the name.
    UPDATE Salesperson
    SET Name = Name + '*'
    WHERE ID IN (
            SELECT salesperson_id
    FROM Orders
            GROUP BY salesperson_id
            HAVING COUNT(*) >= 2
            );

-- c. Deletes all Salesperson that placed orders to the city of Jackson.
    DELETE FROM Salesperson
    WHERE ID IN (
            SELECT DISTINCT o.salesperson_id
                    FROM Orders o
                    INNER JOIN Customer c ON o.customer_id = c.ID
                    WHERE c.City = 'Jackson'
    );

-- d. The total sales amount for each Salesperson.
            -- If the salesperson hasn't sold anything, show zero.
    SELECT
    s.Name,
    COALESCE(SUM(o.Amount), 0) as TotalSales
    FROM Salesperson s
    LEFT JOIN Orders o ON s.ID = o.salesperson_id
    GROUP BY s.ID, s.Name
    ORDER BY s.Name;

-- Versão alternativa mostrando também o ID do vendedor:
    SELECT
    s.ID,
    s.Name,
    COALESCE(SUM(o.Amount), 0) as TotalSales
    FROM Salesperson s
    LEFT JOIN Orders o ON s.ID = o.salesperson_id
    GROUP BY s.ID, s.Name
    ORDER BY s.ID;

}
