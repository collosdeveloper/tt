We need you to build example app which could show your programming skills.

General notes:
native Android language - Java / Kotlin
MVVM/MVI/MVP/Clean or other architecture preferred by your choise
Network requests should be done by Retrofit and some tool for multithreading RxJava/Kotlin coroutines/etc.
Tests are good but not required, preferably UI tests only
No memory leaks/retain rotation state
Error handling for each failed request
Note that API response is same for any sent param, it's just a mock
Example API error response: http://demo5151808.mockable.io/error

1. Login page

POST http://demo5151808.mockable.io/login%3Femail=test@gmail.com&password=testpassword

Submit button should become enabled only when both fields are valid.
Email validation - regular email
Passsword - at least 6 characters without spaces

Correct keyboard types, input chain(next button), TextInputLayout should be used for input fields.

Response has 'token' value which should be used for all other API requests as header param.
Auth state should be saved between launches and go directly to Landing page if user is already logged in.

-- UI --
(email field)
(password field)
[loading indicator]

[Submit button]


2. Landing
GET https://demo5151808.mockable.io/videos%3Fpage=1&searchQuery=test
App should have 'load more' functionality and sending page number in request.

-- UI --
(search field)
[logout button]
[list/grid button] 
[video item1]
[video item2]
[video item3]
[video item4]

2.1 Video item raw should contain:
thumb image
name
description (only in List mode)
posted date in human format (just now, 10 min ago, today, etc...)

On selection open details page (3)

2.2 List/Grid should switch view mode from plain list to 2 columns grid. 
In list mode cell should have dynamic height, depending on video description length.

2.3 Search should invoke API call with "searchQuery" set to new text value.
2.4 Logout button should prompt for confirmation and return to (1) page with clearing token value from preferences.




3. Video details
GET http://demo5151808.mockable.io/video/lXQ6n/

-- UI -- Similar to YouTube page
(video playback view) with placeholder image (thumb_url) while video is not playing. Auto start video from url.

Video name, Author name, Posted date(absolute)
video description (dynamic height)

[comment item 1]
[comment item 2]
[comment item 3]

Transition between elements Video thumbnail