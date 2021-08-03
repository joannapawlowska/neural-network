# Neural network
> Simple neural network library

## General information
- Allows creation of artificial neural networks with possibility of training selection, application of pruning and comparing results of trainings


## Technologies used
- Java 11


## Features
- Loading .txt or .csv files with datasets
- Setting training data to test data ratio
- Adjusting multilayer perceptrons neural network architecture
- Selecting training from back propagation, genetic algorithm, particle swarm optimization
- Adjusting trainings parameters
- Application of neuron pruning


## Usage
    NeuralNetwork network = new NeuralNetworkBuilder()
            .inputLayer(new Layer(13))
            .hiddenLayer(new Layer(24))
            .hiddenLayer(new Layer(18))
            .hiddenLayer(new Layer(16))
            .hiddenLayer(new Layer(10))
            .outputLayer(new Layer(2))
            .activationFunction(new TanH(0.25))
            .dataSet(DataSet.builder()
                     .trainingDataRatioToTestData(0.7)
                     .readFromFile(Path.of("dataset.csv"))
                     .build())
            .build();

    Training t = new BackPropagationBuilder()
                .neuralNetwork(network)
                .writePath((Path.of("results.txt")))
                .epochs(5000)
                .learningRate(0.01)
                .pruning(new PruningBuilder()
                        .minLayerSize(1)
                        .removalThreshold(0.001)
                        .build())
                .build();

        t.perform();


## To do
- Implementation of training comparison