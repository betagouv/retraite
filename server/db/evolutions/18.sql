# --- !Ups

SET GLOBAL log_bin_trust_function_creators=1;
DROP FUNCTION IF EXISTS fnStripTags;
CREATE FUNCTION fnStripTags( textToProcess varchar(6000) )
RETURNS varchar(6000)
DETERMINISTIC 
BEGIN
  DECLARE iStart, iEnd, iSlash, iLength int;;
  DECLARE iStart2, iEnd2, iLength2 int;;
  DECLARE tag text;;
    SET iStart = 1;;	
    WHILE Locate( '<', textToProcess, iStart) > 0 And Locate('>', textToProcess, Locate('<', textToProcess, iStart)) > 0 DO
      BEGIN
        SET iStart = Locate( '<', textToProcess, iStart);;
        SET iEnd = Locate( '>', textToProcess, iStart);;
        SET iSlash = Locate( '/', textToProcess, iStart);;
        SET iLength = ( iEnd - iStart) + 1;;
        IF iLength > 0 THEN
          BEGIN
            SET iStart2 = iStart;;
            IF iSlash = iStart+1 THEN
              SET iStart2 = iStart2+1;;
            END IF;;
            SET iEnd2 = iEnd;;
            IF iSlash = iEnd-1 THEN
              SET iEnd2 = iEnd2-1;;
            END IF;;
            SET iLength2 = ( iEnd2 - iStart2) + 1;;
            SET tag = LOWER(SUBSTR(textToProcess, iStart2+1, iLength2-2));;
            IF tag <> 'p' AND tag <> 'br' AND tag <> 'b' AND tag <> 'ul' AND tag <> 'ol' AND tag <> 'li' AND tag <> 'li' THEN
              # On supprime ce tag 
              SET textToProcess = Insert(textToProcess, iStart, iLength, '');;
            ELSE
              # On laisse ce tag
              SET iStart = iStart+iLength;;
            END IF;;
          END;;
        END IF;;
      END;;
    END WHILE;;
    SET textToProcess = Concat('<p>', textToProcess, '</p>');;
    RETURN textToProcess;;
END;

UPDATE Chapitre set texteActions=fnStripTags(texteIntro);
UPDATE Chapitre set texteModalites=fnStripTags(parcours) WHERE parcoursDematDifferent=0;
UPDATE Chapitre set texteModalites=CONCAT("<p><b>*** Parcours canaux traditionnels ***</b> :</p>",fnStripTags(parcours),"<p></p><p><b>*** Parcours dématérialisé ***</b> :</p>",fnStripTags(parcoursDemat)) WHERE parcoursDematDifferent=1;
UPDATE Chapitre set texteInfos=fnStripTags(texteComplementaire);

# --- !Downs

