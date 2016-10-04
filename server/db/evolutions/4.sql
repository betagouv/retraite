# --- !Ups

create table ChapitreCondition (
    id bigserial not null,
    chapitre_id bigint references Chapitre (id),
    primary key (id)
);

create table ConditionProps (
    condition_id bigint not null references ChapitreCondition (id),
    cle varchar(255) not null,
    valeur varchar(255),
    primary key (condition_id, cle)
);

CREATE INDEX FK_ddlf6p8ayygvfe0oir25yq4fs ON ChapitreCondition USING hash (chapitre_id);

CREATE INDEX FK_eyl1kkmha8hv2t7tdguw7e65n ON ConditionProps USING hash (condition_id);

# --- !Downs

DROP INDEX FK_eyl1kkmha8hv2t7tdguw7e65n;

DROP INDEX FK_ddlf6p8ayygvfe0oir25yq4fs;

drop table ConditionProps;
drop table Condition;