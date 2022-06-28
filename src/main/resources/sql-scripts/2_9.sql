SELECT SUM(titles) / COUNT(cds)  as avg_title_count FROM
    (SELECT distinct cd_id as cds, COUNT(title_id) as titles
     FROM cd_title
     GROUP BY cd_id) as cd_titles;