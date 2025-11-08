package com.example.codesnack.playground

import com.example.codesnack.model.ProgrammingLanguage

object CodeTemplates {

    fun getTemplate(language: ProgrammingLanguage): String {
        return when (language) {
            ProgrammingLanguage.KOTLIN -> """
                fun main() {
                    // Write your Kotlin code here
                    println("Hello from Kotlin!")
                }
            """.trimIndent()

            ProgrammingLanguage.JAVA -> """
                public class Main {
                    public static void main(String[] args) {
                        // Write your Java code here
                        System.out.println("Hello from Java!");
                    }
                }
            """.trimIndent()

            ProgrammingLanguage.PYTHON -> """
                # Write your Python code here
                print("Hello from Python!")
            """.trimIndent()

            ProgrammingLanguage.JAVASCRIPT -> """
                // Write your JavaScript code here
                console.log("Hello from JavaScript!");
            """.trimIndent()

            ProgrammingLanguage.CPP -> """
                #include <iostream>
                #include <string>
                #include <vector>
                using namespace std;

                int main() {
                    // Write your C++ code here
                    cout << "Hello from C++!" << endl;
                    return 0;
                }
            """.trimIndent()

            ProgrammingLanguage.SWIFT -> """
                import Foundation

                // Write your Swift code here
                print("Hello from Swift!")
            """.trimIndent()

            ProgrammingLanguage.RUST -> """
                fn main() {
                    // Write your Rust code here
                    println!("Hello from Rust!");
                }
            """.trimIndent()

            ProgrammingLanguage.GO -> """
                package main
                import "fmt"

                func main() {
                    // Write your Go code here
                    fmt.Println("Hello from Go!")
                }
            """.trimIndent()

            ProgrammingLanguage.DART -> """
                void main() {
                    // Write your Dart code here
                    print('Hello from Dart!');
                }
            """.trimIndent()
        }
    }

    fun getDescription(language: ProgrammingLanguage): String {
        return when (language) {
            ProgrammingLanguage.KOTLIN -> "Write your code inside the main() function. Imports are not needed for basic operations."
            ProgrammingLanguage.JAVA -> "Write your code inside the main() method of the Main class."
            ProgrammingLanguage.PYTHON -> "Python doesn't require a main function. Write your code directly."
            ProgrammingLanguage.JAVASCRIPT -> "JavaScript runs code directly. Use console.log() to print output."
            ProgrammingLanguage.CPP -> "Write your code inside main(). Common headers are already included."
            ProgrammingLanguage.SWIFT -> "Write your code after the import. Use print() for output."
            ProgrammingLanguage.RUST -> "Write your code inside main(). Use println!() macro for output."
            ProgrammingLanguage.GO -> "Write your code inside main(). Package and fmt import are included."
            ProgrammingLanguage.DART -> "Write your code inside main(). Use print() for output."
        }
    }
}