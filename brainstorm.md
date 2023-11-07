# Entities

## Event

### Event table

- ID : **PK**  ```int```
- Label : ```string```
- startDate : ```Date```
- endDate: ```Date```
- Description : ```String Nullable```
- Wikipedia : ```String nullable```
- Diaporama: ```String nullable```
- Images: ```List<Image>```
- Countries: ```List<Country>```
- Keywords: ```List<Keyword>```
- Locations: ```List<Locations>```
- KeyDates: ```List<KeyDate>```
- Types: ```List<Type>```
- Participants: ```List<Participant>```


### Image table

- ID_event: ```Int FK```
- Link : ```String UNIQUE```
- ID_image: ```Int PK serial```


### Country

- ID_country: ```Int PK Serial```
- name: ```String```

### Keyword

- ID: ```Int PK serial```
- label: ```String```

### Locations 

- ID: ```Int PK```
- label: ```String```

### KeyDate

- ID: ```int PK```
- date: ```Date```

### Participant

- ID : ```Int PK```
- name: ```String```

### Kind

- ID: ````Int PK````
- type: ````String````

## Users

### User table

- ID : ```Int PK```
- Events: ```List<Event>```
- Language: ```'FR'|'ENG' default 'FR'```