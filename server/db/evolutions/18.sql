# --- !Ups

SET GLOBAL log_bin_trust_function_creators=1;
DROP FUNCTION IF EXISTS fnStripTags;
DELIMITER |
CREATE FUNCTION fnStripTags( textWithTags varchar(4000) )
RETURNS varchar(4000)
DETERMINISTIC 
BEGIN
  DECLARE iStart, iEnd, iLength int;
  DECLARE tag text;
    WHILE Locate( '<', textWithTags ) > 0 And Locate( '>', textWithTags, Locate( '<', textWithTags )) > 0 DO
      BEGIN
        SET iStart = Locate( '<', textWithTags ), iEnd = Locate( '>', textWithTags, Locate('<', textWithTags ));
        SET iLength = ( iEnd - iStart) + 1;
        IF iLength > 0 THEN
          BEGIN
            SET tag = SUBSTR(textWithTags, iStart, iLength);
            IF tag = '</p>' OR tag = '<br>' OR tag = '<br/>' THEN 
              SET textWithTags = Insert(textWithTags, iStart, iLength, '\n');
            ELSEIF tag = '<li>' THEN 
              SET textWithTags = Insert(textWithTags, iStart, iLength, '-');
            ELSE 
              SET textWithTags = Insert(textWithTags, iStart, iLength, '');
            END IF;
          END;
        END IF;
      END;
    END WHILE;
    RETURN textWithTags;
END;
|
DELIMITER ;

UPDATE Chapitre set texteActions=fnStripTags(texteIntro);
UPDATE Chapitre set texteModalites=fnStripTags(parcours) WHERE parcoursDematDifferent=0;

UPDATE Chapitre set texteInfos=fnStripTags(texteComplementaire);

# --- !Downs

