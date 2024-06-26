SELECT Н_ГРУППЫ_ПЛАНОВ.ПЛАН_ИД FROM Н_ГРУППЫ_ПЛАНОВ 
RIGHT JOIN Н_ПЛАНЫ ON
    Н_ГРУППЫ_ПЛАНОВ.ПЛАН_ИД = Н_ПЛАНЫ.ИД
RIGHT JOIN Н_ФОРМЫ_ОБУЧЕНИЯ ON
    Н_ПЛАНЫ.ФО_ИД = Н_ФОРМЫ_ОБУЧЕНИЯ.ИД
WHERE Н_ФОРМЫ_ОБУЧЕНИЯ.НАИМЕНОВАНИЕ = 'Заочная'
GROUP BY Н_ГРУППЫ_ПЛАНОВ.ПЛАН_ИД
HAVING COUNT(DISTINCT Н_ГРУППЫ_ПЛАНОВ.ГРУППА) < 2;