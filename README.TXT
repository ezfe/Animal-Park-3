Animal Park - Ezekiel Elin - October 15, 2016

The project contains 12 classes, which serve the following purpose, in approx. order of importance

Controller: contains main() and populates the park based on the config file

Park: holds the data for the park and maintains easy relations between cells

Cell: holds a plant, an animal, both, or neither

Organism: abstract class for plants/animals

Plant/Animal: classes to be instantiated into park

PropertiesFile: represents the data in the config.txt

SpeciesProperties: represents the data in a species line in the config.txt

Point: represents an x,y location

Wheel: Random number generator

CellDirection: directions in relation to cell

EventLogItem: An event that occurred during the tick