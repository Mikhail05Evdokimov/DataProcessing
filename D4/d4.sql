select f.departure_airport, f.arrival_airport, fare_conditions seat_class, 
extract(isodow from f.scheduled_departure) day_of_week, extract(hour from f.scheduled_departure) hour_, 
extract(minute from f.scheduled_departure) minute_, flight_no,  avg(tf.amount) from ticket_flights tf 
join flights f 
on f.flight_id = tf.flight_id 
group by f.departure_airport, f.arrival_airport, fare_conditions, extract(isodow from f.scheduled_departure), 
extract(hour from f.scheduled_departure), extract(minute from f.scheduled_departure), f.flight_no