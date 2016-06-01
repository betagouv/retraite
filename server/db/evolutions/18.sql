# --- !Ups

SET GLOBAL log_bin_trust_function_creators=1;
DROP FUNCTION IF EXISTS fnStripTags;
CREATE FUNCTION fnStripTags( textToProcess varchar(6000) )
RETURNS varchar(6000)
DETERMINISTIC 
BEGIN
  DECLARE iStart, iEnd, iLength int;;
  DECLARE tag text;;
    SET iStart = 1;;	
    WHILE Locate( '<', textToProcess, iStart) > 0 And Locate('>', textToProcess, Locate('<', textToProcess, iStart)) > 0 DO
      BEGIN
        SET iStart = Locate( '<', textToProcess, iStart);;
        SET iEnd = Locate( '>', textToProcess, Locate('<', textToProcess, iStart));;
        SET iLength = ( iEnd - iStart) + 1;;
        IF iLength > 0 THEN
          BEGIN
            SET tag = SUBSTR(textToProcess, iStart, iLength);;
            IF tag = '</p>' OR tag = '<br>' OR tag = '<br/>' THEN 
              SET textToProcess = Insert(textToProcess, iStart, iLength, '</p><p>');;
              SET iStart = iStart+7;;
            ELSEIF tag = '<li>' THEN 
              SET textToProcess = Insert(textToProcess, iStart, iLength, '</p><p>- ');;
              SET iStart = iStart+9;;
            ELSE 
              SET textToProcess = Insert(textToProcess, iStart, iLength, '');;
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
UPDATE Chapitre set texteModalites=CONCAT("*** Parcours canaux traditionnels *** :\n",fnStripTags(parcours),"\n\n*** Parcours dématérialisé *** :\n",fnStripTags(parcoursDemat)) WHERE parcoursDematDifferent=1;
UPDATE Chapitre set texteInfos=fnStripTags(texteComplementaire);

# --- !Downs

