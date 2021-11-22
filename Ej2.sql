CREATE DEFINER=`saul`@`%` PROCEDURE `Ej2`(IN afiId INT(11))
BEGIN
	select affiliations.id,affiliation,count(id_character) 
    from affiliations,character_affiliations 
where affiliations.id=character_affiliations.id_affiliation 
and affiliations.id>=afiId group by affiliation
order by affiliations.id;
END