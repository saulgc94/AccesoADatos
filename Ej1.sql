CREATE DEFINER=`saul`@`%` PROCEDURE `Ej1`(IN afiId INT(11),OUT nameAfi VARCHAR(32),
OUT numPjAfi INT)
BEGIN
	DECLARE na VARCHAR(32) DEFAULT null;
	DECLARE numPJ INT DEFAULT NULL;
	select affiliation INTO na from affiliations where id=afiId;
    select count(id_character) INTO numPJ from character_affiliations 
where id_affiliation=afiId;
	SET nameAfi = na;
    SET numPjAfi = numPJ;
END