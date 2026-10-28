#include <cmath>
#include <cstdlib>
#include <ctime>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <random> // modern random
#include <string>

struct FunctionConfig {
    std::string name;
    double a;
    double b;
    double M;
    double exactIntegral;
};

bool getFunctionConfig(const std::string& name, FunctionConfig& config) {
    if (name == "fourth_root") {
        config = {"fourth_root_M", 0.0, 16.0, 30.0, 128.0};
        return true;
    }
    if (name == "abs_cos") {
        config = {"abs_cos", 0.0, M_PI, 1.0, 2.0};
        return true;
    }
    if (name == "polynomial") {
        config = {"polynomial", 1.0, 2.0, 0.5184, 0.25};
        return true;
    }
    if (name == "ln2") {
        config = {"ln2", 1.0, 2.0, 1.0, std::log(2.0)};
        return true;
    }
    if (name == "pi") {
        config = {"pi", 0.0, 1.0, 4.0, M_PI};
        return true;
    }
    return false;
}

double evaluate(const FunctionConfig& config, double x) {
    if (config.name == "fourth_root_M") {
        return 5.0 * std::sqrt(std::sqrt(x));
    }
    if (config.name == "abs_cos") {
        return std::abs(std::cos(x));
    }
    if (config.name == "polynomial") {
        return 15.0 * std::pow(x - 1.0, 2.0) * std::pow(2.0 - x, 3.0);
    }
    if (config.name == "ln2") {
        return 1.0 / x;
    }
    return 4.0 / (1.0 + x * x);
}

void printUsage(const char* program) {
    std::cerr << "Usage: " << program << " <function> [repetitions]\n"
              << "Functions: fourth_root, abs_cos, polynomial, ln2, pi\n"
              << "Repetitions: 5 or 50 (default: 50)\n";
}

int main(int argc, char* argv[]) {
    if (argc < 2 || argc > 3) {
        printUsage(argv[0]);
        return 1;
    }

    FunctionConfig config;
    if (!getFunctionConfig(argv[1], config)) {
        std::cerr << "Unknown function: " << argv[1] << "\n";
        printUsage(argv[0]);
        return 1;
    }

    int repetitions = 50;
    if (argc == 3) {
        try {
            repetitions = std::stoi(argv[2]);
        } catch (const std::exception&) {
            std::cerr << "Repetitions must be either 5 or 50.\n";
            return 1;
        }
        if (repetitions != 5 && repetitions != 50) {
            std::cerr << "Repetitions must be either 5 or 50.\n";
            return 1;
        }
    }

    std::srand(static_cast<unsigned int>(std::time(nullptr))); // classic rand()
    // std::random_device randomDevice; // modern random
    // std::mt19937 generator(randomDevice()); // modern random
    // std::uniform_real_distribution<double> xDistribution(config.a, config.b); // modern random
    // std::uniform_real_distribution<double> yDistribution(0.0, config.M); // modern random

    std::ofstream outputFile("results.csv");
    if (!outputFile) {
        std::cerr << "Could not open results.csv for writing.\n";
        return 1;
    }

    outputFile << std::setprecision(17);
    outputFile << "# function," << config.name << "\n";
    outputFile << "# a," << config.a << "\n";
    outputFile << "# b," << config.b << "\n";
    outputFile << "# M," << config.M << "\n";
    outputFile << "# exact_integral," << config.exactIntegral << "\n";
    outputFile << "# repetitions," << repetitions << "\n";
    outputFile << "n,result\n";

    for (int n = 50; n <= 5000; n += 50) {
        for (int repetition = 0; repetition < repetitions; ++repetition) {
            int count = 0;
            for (int sample = 0; sample < n; ++sample) {
                const double x =
                    config.a + (config.b - config.a) *
                                   (static_cast<double>(std::rand()) / RAND_MAX); // classic rand()
                const double y =
                    config.M * (static_cast<double>(std::rand()) / RAND_MAX); // classic rand()

                // if (evaluate(config, xDistribution(generator)) >= // modern random
                //     yDistribution(generator)) { // modern random
                if (evaluate(config, x) >= y) { // classic rand()
                    ++count;
                }
            }

            const double result =
                (static_cast<double>(count) / n) * (config.b - config.a) * config.M;
            outputFile << n << "," << result << "\n";
        }
    }

    std::cout << "Results saved to results.csv\n";
    return 0;
}
