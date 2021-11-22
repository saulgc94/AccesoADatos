CREATE DEFINER=`saul`@`%` FUNCTION `Ej3`(idFilm INT(11), namePJ VARCHAR(32)) RETURNS int(11)
BEGIN
	DECLARE numM int;
	 select count(deaths.id_character) INTO numM from deaths,characters 
where deaths.id_killer=characters.id and characters.name like namePJ 
and id_film=idFilm;
	RETURN numM;
END