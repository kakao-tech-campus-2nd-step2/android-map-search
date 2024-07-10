# android-map-search

## Layout requirements

**Save search keyword**

- Search every time a character is entered
- When clicked x button, string is erased

**Saved search keyword list**

- Words are not duplicated and recently serached word are added later
- When clicked x button, saved search word is erased

**Search result list**

- There are at least 15 search results
- Search results have search word as categories
- Selected item is added to the saved search word list
- Using `SQLite` to saved search word
    - When application is restart, data is maintained

## Function List

**Save search keyword**

Requirements Rule

- Using `SQLite` to save search data
    - When application is restart, data is maintained
- Apply the MVVM architectural pattern

Input search keyword

- When application is restart, data is maintained
- Search every time a character is entered
- When clicked x button, string is erased

Saved search keyword list

- Words are not duplicated and recently serached word are added later
- When clicked x button, saved search word is erased

Search result list

- There are at least 15 search results
- Search results have search word as categories
- Selected item is added to the saved search word list