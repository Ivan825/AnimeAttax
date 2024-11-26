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
extern "C" JNIEXPORT jobject JNICALL Java_com_example_consolecardgame_GameLogicJavaCallers_Random_getRandomCharacter(JNIEnv *env, jobject obj, jstring filename) {
    static bool isSeeded = false;
    if (!isSeeded) {
        srand(time(0)); // Seed random number generator
        isSeeded = true;
    }

    // Convert Java String to C++ string
    const char *nativeString = env->GetStringUTFChars(filename, 0);
    string fileNameStr(nativeString);
    env->ReleaseStringUTFChars(filename, nativeString);

    // Load JSON data
    json jsonData = loadJsonData(fileNameStr);

    if (jsonData.empty()) {
        return nullptr; // Return null if JSON is empty
    }

    // Create a new ArrayList in Java
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jobject arrayList = env->NewObject(arrayListClass, arrayListConstructor);

    // Get the add() method for ArrayList
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    // Retrieve random character data and populate ArrayList
    int randomIndex = rand() % jsonData.size();
    json character = jsonData[randomIndex];

    // Add each field of the character to the ArrayList
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(character["Character"].get<string>().c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(character["Anime"].get<string>().c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(to_string(character["Attack"].get<int>()).c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(to_string(character["Resilience"].get<int>()).c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(to_string(character["Intelligence"].get<int>()).c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(to_string(character["Buff"].get<int>()).c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(to_string(character["Power"].get<int>()).c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(to_string(character["Armor"].get<int>()).c_str()));
    env->CallBooleanMethod(arrayList, arrayListAdd, env->NewStringUTF(character["Type"].get<string>().c_str()));

    // Return the populated ArrayList
    return arrayList;
}
