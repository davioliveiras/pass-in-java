CREATE UNIQUE INDEX event_slug_key ON event(slug);
CREATE UNIQUE INDEX attendee_event_id_email_key ON attendee(event_id, email);
CREATE UNIQUE INDEX checkin_attendee_id_key ON checkin(attendee_id)