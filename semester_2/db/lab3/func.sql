CREATE OR REPLACE FUNCTION count_people_in_the_crew(target_crew_id INTEGER)
RETURNS INTEGER AS $count_people_in_the_crew$
DECLARE
    people_count INTEGER := 0;
BEGIN
    SELECT COUNT(*) INTO people_count FROM humans WHERE crew_id = target_crew_id;
    RETURN people_count;
END;
$count_people_in_the_crew$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_crew_count()
RETURNS TRIGGER AS $update_crew_count$
BEGIN
    IF TG_OP = 'TRUNCATE' THEN
        UPDATE crews SET people_count = 0;
    END IF;
    
    IF TG_OP = 'DELETE' THEN
        UPDATE crews SET people_count = count_people_in_the_crew(OLD.crew_id) WHERE crews.id = OLD.crew_id;
    ELSE
        IF NEW.crew_id IS NOT NULL THEN
            UPDATE crews SET people_count = count_people_in_the_crew(NEW.crew_id) WHERE crews.id = NEW.crew_id;
        END IF;
    END IF;
    RETURN NEW;
END;
$update_crew_count$ LANGUAGE plpgsql;

CREATE TRIGGER update_count
AFTER INSERT OR UPDATE OR DELETE ON humans
FOR EACH ROW
EXECUTE FUNCTION update_crew_count();

CREATE TRIGGER truncate_count
AFTER TRUNCATE ON humans
EXECUTE FUNCTION update_crew_count();