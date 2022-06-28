SELECT DISTINCT person_name from person p natural join author a natural join dvd_person d natural join cd_artist cd
WHERE p.person_id = a.person_id AND p.person_id = d.person_id AND cd.artist_id = p.person_id
ORDER BY person_name;