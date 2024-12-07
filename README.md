# Software engineering project
## Group A

- Jan Tacakiewicz 128094 -> [Epim3dium](https://github.com/Epim3dium)
- Miguel Raamsdonk 110722 -> [magvr78290](https://github.com/magvr78290)
- Gonçalo Moreira Pires 111259 -> [gmp09]( https://github.com/gmp08)
- Joel Matos 111679 -> [JMaxtos](https://github.com/JMaxtos)

# Features
- Upload Data: Allows you to upload a CSV file with data from the rural property register in Portugal. A CSV file is provided with data from the autonomous region of Madeira.

- Graph Representations: The rural property register, where the nodes represent the properties and the edges represent the adjacency relationships between the nodes (between the properties),is represented in a graph. It can be visualised on the User Interface

- Average Property Area: Calculates the average area of properties, for a geographical/administrative area specified by the user (parish, municipality, district). Also calculates the average area of properties assuming that adjacent properties of the same owner are considered to be just one property, for a geographical/administrative area specified by the user.

- Graph of the neighbourhood relationship between owners: Represents in a graph the neighbourhood relations between owners where the nodes correspond to the owners and the edges represent the neighbourhood relations between the nodes. A landowner is considered to be a neighbour of another landowner if they have contiguous properties.

- Property Exchange Suggestions: Generates suggestions for property exchanges between owners that maximise the average area of each owner's property.


## Used Technologies
- Java: Programming language used for development.
- Maven: Dependency management of the libraries that are used.
- JUnit: Tests and evaluation of test coverage.
- Git/GitHub: Local version control and remote repository for project collaboration.
- Trello: Project management according to the Scrum approach, with tracking between the user story cards and the respective events on GitHub.
- JavaDoc: Documentation of the software produced.

## Extra Features:
### Visualizer
We added an additional visualizer functionality that is defined in the main GUI menu as Structure 1.
There every node is blue color and represents each distinct property, after zooming in the property id
should become visible. ![img.png](img.png)  
Each black line represents a neighbourhood relation between two properties. After zooming in even more
the owner ID of each property will be visible.


![img_1.png](img_1.png)
#### GUI:
* If we want to see each property border we can toggle 'Show outlines'
* If the user finds the ID: and Own: labels intrusive it's also togglable with 'Hide Labels'
* If the user want's to highlight specific Owners properties, they can input owner ID in 'Filter for owner:'
  field and press enter. Then all of this owners' properties will be highlighted with different color:


  ![img_2.png](img_2.png)
#### Suggestions
Visualizer is also used in the suggestions tab. After clicking on specific suggestion row, both of properties that are considered
in this exchange will be highlighted (Magenta color) in new Visuzalizer window.
![img_3.png](img_3.png)

# Additional(s) Note(s)
- The JavaDocs can only be generated with the Maven Dependecy and not by the IDE, but they can be seen on the JavaDocs folder. 
