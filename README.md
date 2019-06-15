# LibCal Client

[LibCal](https://ask.springshare.com/libcal) is an online booking system used to reserve, check bookings and availabilities, and manage booking spaces.

## Requirements

- Retrieve a valid ClientID and ClientSecret from your Admin with the necessary API permissions.
- Retrieve the host of your LibCal instance. E.g https://usyd.libcal.com.

## Usage

```java
LibCalClient libCalClient = new LibCalClient();
String accessToken = libCalClient.getAccessToken(host, clientID, clientSecret).getAccessToken();
SpaceLocation[] spaceLocations = libCalClient.getSpaceLocations(host, accessToken);

// Or use a BookingService to do common booking functionality.

LibCalBookingService libCalBookingService = new LibCalBookingService(host, libCalClient, clientID, clientSecret);
libCalBookingService.getBookableSpaceIDs(categoryIDs, spaceIDs, accessToken, dateStart, dateEnd);
```

## Features

### API

- [X] Locations
- [ ] Form for Space ID
- [ ] Form Question by Question IDs
- [X] Location Categories by Location IDs
- [X] Location Category Spaces
- [X] Reserve Space by Space ID
- [ ] Booking Information by Booking ID
- [ ] All Bookings Information by Date
- [X] Cancel Space Booking
- [ ] All Confirmed Bookings Information 

### Service

- [ ] Automatically retrieve new Access Tokens on expire.
- [X] Get available Spaces for Category.
- [X] Get all Spaces and their descriptions for every Location or Category.

## License

LibCal Client is available under the MIT license. [See LICENSE](https://github.com/philip-bui/libcal-client/blob/master/LICENSE) for details.
