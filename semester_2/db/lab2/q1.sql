SELECT Н_ЛЮДИ.ИД, Н_ВЕДОМОСТИ.ЧЛВК_ИД FROM Н_ЛЮДИ 
RIGHT JOIN Н_ВЕДОМОСТИ ON Н_ЛЮДИ.ИД = Н_ВЕДОМОСТИ.ЧЛВК_ИД 
WHERE Н_ЛЮДИ.ФАМИЛИЯ < 'Иванов' AND Н_ВЕДОМОСТИ.ДАТА = '2010.06.18' AND Н_ВЕДОМОСТИ.ДАТА = '1998.01.05';