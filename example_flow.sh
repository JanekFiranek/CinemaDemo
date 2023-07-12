echo "Fetching screenings between 07.07.2027 05:15:00 and 07.07.2027 09:30:00"
curl -v "localhost:8080/screenings?from=1814937300&to=1814952600"; echo # Append newline after each request
echo "Fetching available seats for screening of ID 4"
curl -v "localhost:8080/screenings/4"; echo
echo "Reserving a seat for screening of ID 4, for one adult in row 1 seat 1"
curl -v POST -H "Content-Type: application/json" \
    -d '{"name":"Maria","surname":"Skłodowska-Curie","screeningId":4,"tickets":[{"type":"ADULT","row":1,"seat":1}]}' \
    "localhost:8080/reservations"; echo
