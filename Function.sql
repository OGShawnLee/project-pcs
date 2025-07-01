DROP FUNCTION IF EXISTS get_person_full_name;
CREATE FUNCTION get_person_full_name(
    in_name VARCHAR(64),
    in_paternal_last_name VARCHAR(64),
    in_maternal_last_name VARCHAR(64)
)
    RETURNS VARCHAR(192)
    DETERMINISTIC
BEGIN
    RETURN TRIM(CONCAT(in_name, ' ', in_paternal_last_name, ' ', COALESCE(in_maternal_last_name, '')));
END;