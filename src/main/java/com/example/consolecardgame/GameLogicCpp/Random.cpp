#include <jni.h>
#include <iostream>
#include <fstream>
#include "json.hpp"
#include "../JNIFiles/com_example_consolecardgame_GameLogicJavaCallers_Random.h" // Use the generated header file
#include <cstdlib>
#include <ctime>

using namespace std;
using json = nlohmann::json;

// Function to load JSON data
json loadJsonData(const std::string& filename) {
    ifstream file(filename);
    json jsonData;
    file >> jsonData;
    return jsonData;
}

// JNI Function Implementation
extern "C" JNIEXPORT jstring JNICALL Java_com_example_consolecardgame_GameLogicJavaCallers_Random_getRandomCharacter(JNIEnv *env, jobject obj, jstring filename) {
    static bool isSeeded = false;
    if (!isSeeded) {
        srand(time(0)); // Seed random number generator
        isSeeded = true;
    }

    const char *nativeString = env->GetStringUTFChars(filename, 0);
    string fileNameStr(nativeString);
    env->ReleaseStringUTFChars(filename, nativeString);

    json jsonData = loadJsonData(fileNameStr);

    if (jsonData.empty()) {
        return env->NewStringUTF("No characters available.");
    }

    int randomIndex = rand() % jsonData.size();
    json character = jsonData[randomIndex];
    string result = character["Character"];

    return env->NewStringUTF(result.c_str());
}
