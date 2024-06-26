SELECT Н_УЧЕНИКИ.ГРУППА, AVG(
        CASE 
            WHEN Н_ЛЮДИ.ДАТА_СМЕРТИ <> '9999-09-09' AND Н_ЛЮДИ.ДАТА_СМЕРТИ IS NOT NULL
                THEN Н_ЛЮДИ.ДАТА_СМЕРТИ - Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
            ELSE NOW() - Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
        END
    ) FROM Н_УЧЕНИКИ
RIGHT JOIN Н_ЛЮДИ ON Н_УЧЕНИКИ.ИД = Н_ЛЮДИ.ИД
    GROUP BY Н_УЧЕНИКИ.ГРУППА
HAVING AVG(
        CASE 
            WHEN Н_ЛЮДИ.ДАТА_СМЕРТИ <> '9999-09-09' AND Н_ЛЮДИ.ДАТА_СМЕРТИ IS NOT NULL
                THEN Н_ЛЮДИ.ДАТА_СМЕРТИ - Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
            ELSE NOW() - Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
        END
    ) > (
    SELECT MIN(
        CASE 
            WHEN Н_ЛЮДИ.ДАТА_СМЕРТИ <> '9999-09-09' AND Н_ЛЮДИ.ДАТА_СМЕРТИ IS NOT NULL
                THEN Н_ЛЮДИ.ДАТА_СМЕРТИ - Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
            ELSE NOW() - Н_ЛЮДИ.ДАТА_РОЖДЕНИЯ
        END
    ) FROM Н_УЧЕНИКИ
    RIGHT JOIN Н_ЛЮДИ ON Н_УЧЕНИКИ.ИД = Н_ЛЮДИ.ИД 
    WHERE Н_УЧЕНИКИ.ГРУППА = '3100' AND Н_ЛЮДИ.ДАТА_СМЕРТИ = '9999-09-09' AND Н_ЛЮДИ.ДАТА_СМЕРТИ IS NOT NULL
);
