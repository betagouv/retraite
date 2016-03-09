# --- !Ups

create table PeriodeDepartLegal (
    id bigint not null auto_increment,
    ageDepartLegal_annees integer,
    ageDepartLegal_mois integer,
    ageTauxPlein_annees integer,
    ageTauxPlein_mois integer,
    dateNaissanceDebut varchar(255),
    primary key (id)
);

INSERT INTO PeriodeDepartLegal (dateNaissanceDebut, ageDepartLegal_annees, ageDepartLegal_mois, ageTauxPlein_annees, ageTauxPlein_mois) VALUES 
	("01/01/1955", 62, 0, 67, 0),
	("01/01/1954", 61, 7, 66, 7),
	("01/01/1953", 61, 2, 66, 2),
	("01/01/1952", 60, 9, 65, 9),
	("01/07/1951", 60, 4, 65, 4),
	("", 60, 0, 65, 0);


# --- !Downs

