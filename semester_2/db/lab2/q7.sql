SELECT ИМЯ, COUNT(ИМЯ) FROM Н_ЛЮДИ as Л1 
WHERE EXISTS(SELECT 1 FROM Н_ЛЮДИ as Л2 WHERE Л1.ИМЯ = Л2.ИМЯ AND Л1.ДАТА_РОЖДЕНИЯ <> Л2.ДАТА_РОЖДЕНИЯ AND Л1.ИД <> Л2.ИД)
GROUP BY ИМЯ
ORDER BY ИМЯ;
