# Notes from cleaning up data


### Circuits

* removed url column

### Constructors

* removed url column

### Drivers

* removed url, number and code columns

### Races

* removed url, fp1_date, fp1_time, fp2_date, fp2_time, fp3_date, fp3_time, quali_date, quali_time, sprint_date and sprint_time columns
* replace all "\N" with "" using excels replace all functionality

### Constructor_results

* removed status column

### Constructor_standings

* removed potion_text column

### Driver_standings

* removed potion_text column

### lap_times

* removed time column

### pit_stops

* removed time and duration column

### qualifying

* removed all "\N" with excel remove all functionality
* converted q1, q2,q3 to miliseconds (int) because sql server doesnt support the mm:ss.ss time format

### results

* removed position_text and time column
* removed all "\N" with excel remove all functionality

### sprint_results

* removed position_text and time column
* removed all "\N" with excel remove all functionality
