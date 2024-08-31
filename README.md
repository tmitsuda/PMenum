# PMenum
## Introduction
PMenum is a ProM plug-in tool for enumerating process models exhaustively using heuristicsMiner.
HeuristicsMiner is a control-flow discovery algorithm that generates a control-flow model, which describes the typical sequence of activities recorded in an input log.
Mining with the HeuristicsMiner requires the setting of multiple parameters, but it is not easy to identify the appropriate parameter values to obtain an ideal model.
PMenum can reduce the effort required for users to adjust parameter values to obtain an ideal process model.

## Features
PMenum implements 4 plug-ins:
- Process model transition table generation: converts an input log to an object which records list of parameter values that each generate a different model. The object is used as input for the following three plug-ins.
- Process model visualizer: displays process models that can be generated with parameter values recorded in the process model transition table.
- Parameter value list export: exports list of parameter values in CSV format, which is useful to manually generate process models using HeuristicsMiner plug-in.
- Process model transition table export: exports process model transition table export in CSV format, which is useful to visually check the distribution of process models in parameter space.

## Demo Video
https://drive.google.com/file/d/1-JV5GeIhl2eMe6yhJ2w_xaYoyQl-BKbN/view?usp=sharing

## Installation
PMenum is not yet released in ProM Package Manager.
Deveropment environment is required to install this plug-in.
Please check out [the documentation of ProM](https://promtools.org/development/documentation/how-to-contribute/) and build a development environment to your local computer.

The project can be downloaded from [here](https://github.com/tmitsuda/PMenum/releases/tag/v1.0), or you can simply clone our repository.
Procedures to run the project for the first time is also written in [the documentation of ProM](https://promtools.org/development/documentation/how-to-contribute/).

## Usage
### Display process models on visualizer
+ Import the log using standard ProM feature.
+ Select the log and click "Use resource" button.
+ Select "Generate Model Transition Table for HeuristicsMiner" and click "Start".
+ An object that records the list of parameter values is created and the visualizer automatically launches.
+ each time we click the "next" button or the "prev" button on the right side of the window, the visualizer displays another process model that can be generated from the input logs.

### Export a list of parameter values as CSV format
+ Select the object created above and click "Export to disk" button.
+ Select "Export parameter value list" for the type of file.

### Export a process model transition table as CSV format
+ Select the object created above and click "Export to disk" button.
+ Select "Export process model transition table" for the type of file.

## License
This project is licensed under the terms of the LGPL. See "LICENSE" for details.

This project contains sourse code of HeuristicsMiner package, which is distributed under LGPL.
