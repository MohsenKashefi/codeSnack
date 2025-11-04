package com.example.codesnack.data

import com.example.codesnack.model.CodeSnippet
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.model.SnippetCategory

object SnippetRepository {

    private val snippets = listOf(
        // Kotlin snippets (1-20)
        CodeSnippet(
            id = 1,
            language = ProgrammingLanguage.KOTLIN,
            title = "Elvis Operator for Null Safety",
            code = "val name = user?.name ?: \"Unknown\"",
            explanation = "Use ?: to provide a default value when the left side is null",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 2,
            language = ProgrammingLanguage.KOTLIN,
            title = "Destructuring Declarations",
            code = "val (name, age) = person",
            explanation = "Destructure data classes into multiple variables in one line",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 3,
            language = ProgrammingLanguage.KOTLIN,
            title = "Scope Functions: apply",
            code = "val person = Person().apply {\n  name = \"John\"\n  age = 30\n}",
            explanation = "Use apply for object configuration - returns the object itself",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 4,
            language = ProgrammingLanguage.KOTLIN,
            title = "Smart Casts",
            code = "if (x is String) {\n  println(x.length) // auto-cast!\n}",
            explanation = "Kotlin automatically casts after type checking",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 5,
            language = ProgrammingLanguage.KOTLIN,
            title = "Extension Functions",
            code = "fun String.isEmail() = contains(\"@\")",
            explanation = "Add methods to existing classes without inheritance",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 6,
            language = ProgrammingLanguage.KOTLIN,
            title = "When Expression",
            code = "val result = when(x) {\n  1 -> \"one\"\n  2 -> \"two\"\n  else -> \"other\"\n}",
            explanation = "When is more powerful than switch and returns a value",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 7,
            language = ProgrammingLanguage.KOTLIN,
            title = "Named Arguments",
            code = "createUser(name = \"John\", age = 30)",
            explanation = "Named arguments improve readability and order flexibility",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 8,
            language = ProgrammingLanguage.KOTLIN,
            title = "Data Classes",
            code = "data class User(val name: String)",
            explanation = "Get equals, hashCode, toString, and copy for free",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 9,
            language = ProgrammingLanguage.KOTLIN,
            title = "Lazy Initialization",
            code = "val data by lazy { loadData() }",
            explanation = "Delays expensive initialization until first access",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 10,
            language = ProgrammingLanguage.KOTLIN,
            title = "String Templates",
            code = "val msg = \"Hello \$name, you are \${age + 1}\"",
            explanation = "Embed expressions directly in strings with dollar sign",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 11,
            language = ProgrammingLanguage.KOTLIN,
            title = "Safe Casts",
            code = "val str: String? = obj as? String",
            explanation = "Use as? to avoid ClassCastException, returns null on failure",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 12,
            language = ProgrammingLanguage.KOTLIN,
            title = "Let for Null Checks",
            code = "value?.let { println(it) }",
            explanation = "Execute code only if value is not null",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 13,
            language = ProgrammingLanguage.KOTLIN,
            title = "Companion Object",
            code = "companion object {\n  const val TAG = \"MyClass\"\n}",
            explanation = "Create static-like members in Kotlin classes",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 14,
            language = ProgrammingLanguage.KOTLIN,
            title = "Sealed Classes",
            code = "sealed class Result {\n  data class Success(val data: String): Result()\n}",
            explanation = "Restrict class hierarchies for exhaustive when expressions",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 15,
            language = ProgrammingLanguage.KOTLIN,
            title = "Collection Operators",
            code = "val adults = users.filter { it.age >= 18 }",
            explanation = "Use functional operators for concise collection manipulation",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 16,
            language = ProgrammingLanguage.KOTLIN,
            title = "Inline Functions",
            code = "inline fun <T> measure(block: () -> T): T",
            explanation = "Inline reduces lambda overhead for higher-order functions",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 17,
            language = ProgrammingLanguage.KOTLIN,
            title = "Require and Check",
            code = "require(age >= 0) { \"Age must be positive\" }",
            explanation = "Validate arguments and state with built-in functions",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 18,
            language = ProgrammingLanguage.KOTLIN,
            title = "Ranges",
            code = "if (x in 1..10) println(\"In range\")",
            explanation = "Use ranges for elegant boundary checks",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 19,
            language = ProgrammingLanguage.KOTLIN,
            title = "takeIf and takeUnless",
            code = "val valid = input.takeIf { it.isNotEmpty() }",
            explanation = "Return value if condition is met, otherwise null",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 20,
            language = ProgrammingLanguage.KOTLIN,
            title = "Double Bang Gotcha",
            code = "val name = user!!.name // Can crash!",
            explanation = "Avoid !! operator, it defeats Kotlin's null safety",
            category = SnippetCategory.GOTCHA
        ),

        // Python snippets (21-40)
        CodeSnippet(
            id = 21,
            language = ProgrammingLanguage.PYTHON,
            title = "List Comprehension",
            code = "squares = [x**2 for x in range(10)]",
            explanation = "Create lists in a single, readable line instead of loops",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 22,
            language = ProgrammingLanguage.PYTHON,
            title = "Swap Variables",
            code = "a, b = b, a",
            explanation = "Python's tuple unpacking makes swapping elegant",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 23,
            language = ProgrammingLanguage.PYTHON,
            title = "Dictionary Get with Default",
            code = "value = my_dict.get('key', 'default')",
            explanation = "Avoid KeyError by providing a default value",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 24,
            language = ProgrammingLanguage.PYTHON,
            title = "F-Strings for Formatting",
            code = "msg = f\"Hello {name}, you are {age}\"",
            explanation = "F-strings are faster and more readable than .format()",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 25,
            language = ProgrammingLanguage.PYTHON,
            title = "Enumerate in Loops",
            code = "for i, item in enumerate(items):\n  print(f\"{i}: {item}\")",
            explanation = "Get both index and value without manual counter",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 26,
            language = ProgrammingLanguage.PYTHON,
            title = "Context Managers",
            code = "with open('file.txt') as f:\n  data = f.read()",
            explanation = "Automatically handles resource cleanup even on errors",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 27,
            language = ProgrammingLanguage.PYTHON,
            title = "Multiple Assignment",
            code = "x, y, z = 1, 2, 3",
            explanation = "Assign multiple variables in one line using tuple unpacking",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 28,
            language = ProgrammingLanguage.PYTHON,
            title = "Dictionary Comprehension",
            code = "squares = {x: x**2 for x in range(5)}",
            explanation = "Create dictionaries concisely with comprehension syntax",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 29,
            language = ProgrammingLanguage.PYTHON,
            title = "Walrus Operator",
            code = "if (n := len(items)) > 10:\n  print(f\"Large: {n}\")",
            explanation = "Assign and use a value in the same expression",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 30,
            language = ProgrammingLanguage.PYTHON,
            title = "Unpacking Operators",
            code = "merged = {**dict1, **dict2}",
            explanation = "Use ** to merge dictionaries or * to merge lists",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 31,
            language = ProgrammingLanguage.PYTHON,
            title = "Default Dictionary",
            code = "from collections import defaultdict\nd = defaultdict(list)",
            explanation = "Avoid KeyError by providing default values for missing keys",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 32,
            language = ProgrammingLanguage.PYTHON,
            title = "Zip for Parallel Iteration",
            code = "for name, age in zip(names, ages):\n  print(name, age)",
            explanation = "Iterate over multiple sequences simultaneously",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 33,
            language = ProgrammingLanguage.PYTHON,
            title = "Any and All",
            code = "all_positive = all(x > 0 for x in nums)",
            explanation = "Check if all or any elements meet a condition",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 34,
            language = ProgrammingLanguage.PYTHON,
            title = "Set for Deduplication",
            code = "unique = list(set(items))",
            explanation = "Quickly remove duplicates using set conversion",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 35,
            language = ProgrammingLanguage.PYTHON,
            title = "Ternary Operator",
            code = "result = \"yes\" if condition else \"no\"",
            explanation = "Concise conditional assignment in one line",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 36,
            language = ProgrammingLanguage.PYTHON,
            title = "Mutable Default Arguments",
            code = "def func(items=[]):  # Danger!\n  items.append(1)",
            explanation = "Default mutable arguments persist between calls - use None instead",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 37,
            language = ProgrammingLanguage.PYTHON,
            title = "Chain Comparisons",
            code = "if 0 < x < 10:\n  print(\"In range\")",
            explanation = "Chain multiple comparisons naturally",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 38,
            language = ProgrammingLanguage.PYTHON,
            title = "Lambda Functions",
            code = "sorted_items = sorted(items, key=lambda x: x.age)",
            explanation = "Create small anonymous functions inline",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 39,
            language = ProgrammingLanguage.PYTHON,
            title = "Generator Expressions",
            code = "sum_squares = sum(x**2 for x in range(1000))",
            explanation = "Memory-efficient alternative to list comprehensions",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 40,
            language = ProgrammingLanguage.PYTHON,
            title = "Slice Assignment",
            code = "items[1:3] = ['a', 'b', 'c']",
            explanation = "Replace multiple list elements in one operation",
            category = SnippetCategory.TRICK
        ),

        // JavaScript snippets (41-60)
        CodeSnippet(
            id = 41,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array Destructuring",
            code = "const [first, ...rest] = array",
            explanation = "Extract first element and gather remaining into new array",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 42,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Optional Chaining",
            code = "const city = user?.address?.city",
            explanation = "Safely access nested properties without null checks",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 43,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Nullish Coalescing",
            code = "const value = input ?? 'default'",
            explanation = "Use ?? instead of || to only check for null/undefined",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 44,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array Filter & Map Chain",
            code = "const result = arr\n  .filter(x => x > 0)\n  .map(x => x * 2)",
            explanation = "Chain array methods for readable data transformations",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 45,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Object Destructuring",
            code = "const {name, age} = person",
            explanation = "Extract object properties into variables",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 46,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Spread Operator",
            code = "const merged = {...obj1, ...obj2}",
            explanation = "Merge objects or arrays easily with spread syntax",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 47,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Template Literals",
            code = "const msg = `Hello \${name}!`",
            explanation = "Embed expressions in strings with backticks",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 48,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Arrow Functions",
            code = "const double = x => x * 2",
            explanation = "Shorter syntax and lexical this binding",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 49,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array.includes()",
            code = "if (arr.includes(value)) { }",
            explanation = "Cleaner than indexOf for checking element existence",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 50,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Default Parameters",
            code = "function greet(name = 'Guest') { }",
            explanation = "Set default values for function parameters",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 51,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Short-Circuit Evaluation",
            code = "const name = user && user.name",
            explanation = "Use && and || for conditional operations",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 52,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array.reduce()",
            code = "const sum = arr.reduce((a, b) => a + b, 0)",
            explanation = "Reduce array to single value with accumulator",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 53,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Object.entries()",
            code = "for (const [key, val] of Object.entries(obj)) { }",
            explanation = "Iterate over object key-value pairs easily",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 54,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Promise.all()",
            code = "const results = await Promise.all([p1, p2, p3])",
            explanation = "Execute multiple async operations in parallel",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 55,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array.find()",
            code = "const user = users.find(u => u.id === id)",
            explanation = "Get first element matching condition",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 56,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Async/Await",
            code = "const data = await fetchData()",
            explanation = "Cleaner syntax for handling promises",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 57,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Set for Unique Values",
            code = "const unique = [...new Set(array)]",
            explanation = "Remove duplicates by converting to Set and back",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 58,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "String.padStart()",
            code = "const id = '5'.padStart(3, '0') // '005'",
            explanation = "Pad strings to desired length easily",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 59,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array.some() and every()",
            code = "const hasAdult = users.some(u => u.age >= 18)",
            explanation = "Check if any or all elements meet condition",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 60,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Typeof Gotcha",
            code = "typeof null === 'object' // true!",
            explanation = "typeof null returns 'object' due to legacy bug",
            category = SnippetCategory.GOTCHA
        ),

        // Java snippets (61-80)
        CodeSnippet(
            id = 61,
            language = ProgrammingLanguage.JAVA,
            title = "Streams for Collection Processing",
            code = "list.stream()\n  .filter(x -> x > 0)\n  .collect(Collectors.toList())",
            explanation = "Use streams for declarative collection operations",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 62,
            language = ProgrammingLanguage.JAVA,
            title = "Try-With-Resources",
            code = "try (var reader = new FileReader(file)) {\n  // auto-closes!\n}",
            explanation = "Automatically close resources, preventing leaks",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 63,
            language = ProgrammingLanguage.JAVA,
            title = "Var for Type Inference",
            code = "var list = new ArrayList<String>()",
            explanation = "Use var to reduce verbosity (Java 10+)",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 64,
            language = ProgrammingLanguage.JAVA,
            title = "Optional to Avoid Null",
            code = "Optional<String> opt = Optional.ofNullable(value)",
            explanation = "Explicitly handle presence or absence of values",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 65,
            language = ProgrammingLanguage.JAVA,
            title = "String.join()",
            code = "String result = String.join(\", \", list)",
            explanation = "Join strings with delimiter without loops",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 66,
            language = ProgrammingLanguage.JAVA,
            title = "Lambda Expressions",
            code = "list.forEach(item -> System.out.println(item))",
            explanation = "Write concise anonymous function implementations",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 67,
            language = ProgrammingLanguage.JAVA,
            title = "Method References",
            code = "list.forEach(System.out::println)",
            explanation = "Even shorter than lambdas for method calls",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 68,
            language = ProgrammingLanguage.JAVA,
            title = "Switch Expressions",
            code = "var result = switch(day) {\n  case MON -> \"Monday\";\n  default -> \"Other\";\n}",
            explanation = "Switch can return values (Java 14+)",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 69,
            language = ProgrammingLanguage.JAVA,
            title = "Text Blocks",
            code = "String json = \"\"\"\n  {\"name\": \"John\"}\n  \"\"\"",
            explanation = "Multi-line strings without escape characters (Java 15+)",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 70,
            language = ProgrammingLanguage.JAVA,
            title = "Stream.collect()",
            code = "Map<String, List<User>> byCity = users.stream()\n  .collect(Collectors.groupingBy(User::getCity))",
            explanation = "Group and transform stream results powerfully",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 71,
            language = ProgrammingLanguage.JAVA,
            title = "List.of() for Immutable Lists",
            code = "var list = List.of(\"a\", \"b\", \"c\")",
            explanation = "Create immutable collections concisely (Java 9+)",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 72,
            language = ProgrammingLanguage.JAVA,
            title = "Pattern Matching instanceof",
            code = "if (obj instanceof String s) {\n  System.out.println(s.length());\n}",
            explanation = "Combine type check and cast in one step (Java 16+)",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 73,
            language = ProgrammingLanguage.JAVA,
            title = "Records for Data Classes",
            code = "record Point(int x, int y) { }",
            explanation = "Concise immutable data carriers (Java 14+)",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 74,
            language = ProgrammingLanguage.JAVA,
            title = "Stream.filter() Early",
            code = "list.stream().filter(x -> x != null)\n  .map(x -> x.toString())",
            explanation = "Filter early in stream pipeline for better performance",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 75,
            language = ProgrammingLanguage.JAVA,
            title = "Objects.requireNonNull()",
            code = "this.name = Objects.requireNonNull(name)",
            explanation = "Validate non-null with clear error messages",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 76,
            language = ProgrammingLanguage.JAVA,
            title = "Comparator.comparing()",
            code = "list.sort(Comparator.comparing(User::getAge))",
            explanation = "Create comparators easily with method references",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 77,
            language = ProgrammingLanguage.JAVA,
            title = "String Interning",
            code = "String s1 = \"abc\";\nString s2 = \"abc\"; // s1 == s2",
            explanation = "String literals are automatically interned for memory efficiency",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 78,
            language = ProgrammingLanguage.JAVA,
            title = "CompletableFuture",
            code = "CompletableFuture.supplyAsync(() -> fetchData())\n  .thenApply(data -> process(data))",
            explanation = "Chain asynchronous operations elegantly",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 79,
            language = ProgrammingLanguage.JAVA,
            title = "Static Import",
            code = "import static java.lang.Math.*;\nvar result = sqrt(25)",
            explanation = "Import static methods to use without class prefix",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 80,
            language = ProgrammingLanguage.JAVA,
            title = "Diamond Operator",
            code = "List<String> list = new ArrayList<>()",
            explanation = "Let compiler infer generic types on right side",
            category = SnippetCategory.SHORTCUT
        ),

        // C++ snippets (81-100)
        CodeSnippet(
            id = 81,
            language = ProgrammingLanguage.CPP,
            title = "Range-Based For Loop",
            code = "for (const auto& item : vec) {\n  // process item\n}",
            explanation = "Cleaner syntax than iterator loops (C++11)",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 82,
            language = ProgrammingLanguage.CPP,
            title = "Smart Pointers",
            code = "auto ptr = std::make_unique<MyClass>()",
            explanation = "Use smart pointers to avoid manual memory management",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 83,
            language = ProgrammingLanguage.CPP,
            title = "Auto Type Deduction",
            code = "auto result = calculate()",
            explanation = "Let compiler deduce types for cleaner code",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 84,
            language = ProgrammingLanguage.CPP,
            title = "Lambda Expressions",
            code = "auto sum = [](int a, int b) { return a + b; }",
            explanation = "Create inline functions for algorithms",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 85,
            language = ProgrammingLanguage.CPP,
            title = "Move Semantics",
            code = "vec.push_back(std::move(item))",
            explanation = "Transfer ownership instead of copying for performance",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 86,
            language = ProgrammingLanguage.CPP,
            title = "Structured Bindings",
            code = "auto [x, y] = getPoint()",
            explanation = "Unpack tuples and pairs elegantly (C++17)",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 87,
            language = ProgrammingLanguage.CPP,
            title = "Uniform Initialization",
            code = "std::vector<int> v{1, 2, 3}",
            explanation = "Use {} for consistent initialization syntax",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 88,
            language = ProgrammingLanguage.CPP,
            title = "constexpr for Compile-Time",
            code = "constexpr int square(int x) { return x * x; }",
            explanation = "Evaluate functions at compile time for performance",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 89,
            language = ProgrammingLanguage.CPP,
            title = "Emplace Instead of Push",
            code = "vec.emplace_back(arg1, arg2)",
            explanation = "Construct objects in-place to avoid extra copies",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 90,
            language = ProgrammingLanguage.CPP,
            title = "std::optional",
            code = "std::optional<int> findValue()",
            explanation = "Represent values that may not exist (C++17)",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 91,
            language = ProgrammingLanguage.CPP,
            title = "String View",
            code = "void process(std::string_view str)",
            explanation = "Non-owning string reference avoids copies (C++17)",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 92,
            language = ProgrammingLanguage.CPP,
            title = "RAII Pattern",
            code = "std::lock_guard<std::mutex> lock(mutex)",
            explanation = "Resource acquisition is initialization - automatic cleanup",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 93,
            language = ProgrammingLanguage.CPP,
            title = "Vector Reserve",
            code = "vec.reserve(1000)",
            explanation = "Pre-allocate capacity to avoid reallocations",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 94,
            language = ProgrammingLanguage.CPP,
            title = "Default Member Initialization",
            code = "class Foo {\n  int x = 0;\n}",
            explanation = "Initialize members at declaration (C++11)",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 95,
            language = ProgrammingLanguage.CPP,
            title = "std::array for Fixed Size",
            code = "std::array<int, 5> arr{1, 2, 3, 4, 5}",
            explanation = "Use std::array instead of C arrays for safety",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 96,
            language = ProgrammingLanguage.CPP,
            title = "Using with Namespaces",
            code = "using Vec = std::vector<int>",
            explanation = "Create type aliases for complex types",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 97,
            language = ProgrammingLanguage.CPP,
            title = "nullptr vs NULL",
            code = "int* ptr = nullptr; // Not NULL!",
            explanation = "Always use nullptr instead of NULL or 0",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 98,
            language = ProgrammingLanguage.CPP,
            title = "Override Keyword",
            code = "void func() override { }",
            explanation = "Explicitly mark overridden methods to catch errors",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 99,
            language = ProgrammingLanguage.CPP,
            title = "std::algorithm Functions",
            code = "std::sort(vec.begin(), vec.end())",
            explanation = "Use standard algorithms instead of manual loops",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 100,
            language = ProgrammingLanguage.CPP,
            title = "Copy Elision",
            code = "MyClass func() { return MyClass(); }",
            explanation = "Modern C++ automatically optimizes away copies",
            category = SnippetCategory.TRICK
        ),

        // Swift snippets (101-120)
        CodeSnippet(
            id = 101,
            language = ProgrammingLanguage.SWIFT,
            title = "Guard for Early Exit",
            code = "guard let value = optional else {\n  return\n}",
            explanation = "Guard statements improve code readability",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 102,
            language = ProgrammingLanguage.SWIFT,
            title = "Nil Coalescing",
            code = "let name = username ?? \"Guest\"",
            explanation = "Provide default values for optionals concisely",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 103,
            language = ProgrammingLanguage.SWIFT,
            title = "Optional Chaining",
            code = "let city = user?.address?.city",
            explanation = "Safely navigate optional properties",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 104,
            language = ProgrammingLanguage.SWIFT,
            title = "Map on Collections",
            code = "let doubled = numbers.map { $0 * 2 }",
            explanation = "Transform arrays functionally with map",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 105,
            language = ProgrammingLanguage.SWIFT,
            title = "Trailing Closures",
            code = "animate(duration: 1) {\n  view.alpha = 0\n}",
            explanation = "Clean syntax for closure parameters",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 106,
            language = ProgrammingLanguage.SWIFT,
            title = "If Let Binding",
            code = "if let name = optionalName {\n  print(name)\n}",
            explanation = "Safely unwrap optionals in if statement",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 107,
            language = ProgrammingLanguage.SWIFT,
            title = "String Interpolation",
            code = "let msg = \"Hello \\(name), age \\(age)\"",
            explanation = "Embed expressions directly in strings",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 108,
            language = ProgrammingLanguage.SWIFT,
            title = "Shorthand Argument Names",
            code = "numbers.filter { $0 > 10 }",
            explanation = "Use $0, $1 for closure parameters",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 109,
            language = ProgrammingLanguage.SWIFT,
            title = "Defer for Cleanup",
            code = "defer { file.close() }",
            explanation = "Ensure cleanup code runs when leaving scope",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 110,
            language = ProgrammingLanguage.SWIFT,
            title = "Computed Properties",
            code = "var fullName: String {\n  return \"\\(first) \\(last)\"\n}",
            explanation = "Properties calculated on access instead of stored",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 111,
            language = ProgrammingLanguage.SWIFT,
            title = "Type Inference",
            code = "let name = \"John\" // String inferred",
            explanation = "Swift infers types from initial values",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 112,
            language = ProgrammingLanguage.SWIFT,
            title = "Switch Must Be Exhaustive",
            code = "switch value {\ncase .some: break\ndefault: break\n}",
            explanation = "Swift requires all cases covered in switch",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 113,
            language = ProgrammingLanguage.SWIFT,
            title = "Lazy Properties",
            code = "lazy var data = loadData()",
            explanation = "Delay expensive initialization until first use",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 114,
            language = ProgrammingLanguage.SWIFT,
            title = "Extension Methods",
            code = "extension String {\n  func isEmail() -> Bool { }\n}",
            explanation = "Add methods to existing types",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 115,
            language = ProgrammingLanguage.SWIFT,
            title = "Value vs Reference Types",
            code = "struct Point { } // Value type\nclass Person { } // Reference",
            explanation = "Structs are copied, classes are referenced",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 116,
            language = ProgrammingLanguage.SWIFT,
            title = "Enumeration with Raw Values",
            code = "enum Status: String {\n  case active = \"Active\"\n}",
            explanation = "Associate raw values with enum cases",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 117,
            language = ProgrammingLanguage.SWIFT,
            title = "CompactMap for Filtering Nils",
            code = "let valid = items.compactMap { $0 }",
            explanation = "Map and remove nil values in one step",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 118,
            language = ProgrammingLanguage.SWIFT,
            title = "Property Observers",
            code = "var value: Int {\n  didSet { print(\"Changed\") }\n}",
            explanation = "React to property changes with willSet/didSet",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 119,
            language = ProgrammingLanguage.SWIFT,
            title = "Multiple Optional Binding",
            code = "if let a = optA, let b = optB {\n  // both unwrapped\n}",
            explanation = "Unwrap multiple optionals in one if statement",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 120,
            language = ProgrammingLanguage.SWIFT,
            title = "Result Type",
            code = "func fetch() -> Result<Data, Error>",
            explanation = "Explicit success/failure return types",
            category = SnippetCategory.BEST_PRACTICE
        ),

        // Rust snippets (121-140)
        CodeSnippet(
            id = 121,
            language = ProgrammingLanguage.RUST,
            title = "Pattern Matching",
            code = "match result {\n  Ok(val) => println!(\"{}\", val),\n  Err(e) => eprintln!(\"{}\", e)\n}",
            explanation = "Match is exhaustive and more powerful than if-else",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 122,
            language = ProgrammingLanguage.RUST,
            title = "If Let for Single Pattern",
            code = "if let Some(x) = option {\n  println!(\"{}\", x)\n}",
            explanation = "Cleaner than match for single pattern cases",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 123,
            language = ProgrammingLanguage.RUST,
            title = "Option and Result",
            code = "fn divide(a: i32, b: i32) -> Option<i32>",
            explanation = "Explicit handling of absence and errors",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 124,
            language = ProgrammingLanguage.RUST,
            title = "Question Mark Operator",
            code = "let data = read_file()?",
            explanation = "Propagate errors concisely with ?",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 125,
            language = ProgrammingLanguage.RUST,
            title = "Borrowing with &",
            code = "fn process(data: &Vec<i32>)",
            explanation = "Borrow data without taking ownership",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 126,
            language = ProgrammingLanguage.RUST,
            title = "Mutable Borrows",
            code = "fn modify(data: &mut Vec<i32>)",
            explanation = "Only one mutable borrow allowed at a time",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 127,
            language = ProgrammingLanguage.RUST,
            title = "Vec! Macro",
            code = "let v = vec![1, 2, 3]",
            explanation = "Convenient macro for creating vectors",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 128,
            language = ProgrammingLanguage.RUST,
            title = "Iterator Chains",
            code = "let sum: i32 = vec.iter()\n  .filter(|&x| x > 0)\n  .sum()",
            explanation = "Chain iterator methods for functional style",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 129,
            language = ProgrammingLanguage.RUST,
            title = "Derive Traits",
            code = "#[derive(Debug, Clone)]\nstruct Point { x: i32 }",
            explanation = "Automatically implement common traits",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 130,
            language = ProgrammingLanguage.RUST,
            title = "String vs &str",
            code = "let s: String = String::from(\"hello\")\nlet r: &str = \"hello\"",
            explanation = "String owns data, &str is a borrowed slice",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 131,
            language = ProgrammingLanguage.RUST,
            title = "Unwrap_or for Defaults",
            code = "let value = option.unwrap_or(0)",
            explanation = "Provide default value for None",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 132,
            language = ProgrammingLanguage.RUST,
            title = "Lifetime Annotations",
            code = "fn longest<'a>(x: &'a str, y: &'a str) -> &'a str",
            explanation = "Explicit lifetimes prevent dangling references",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 133,
            language = ProgrammingLanguage.RUST,
            title = "Closure Syntax",
            code = "let add = |a, b| a + b",
            explanation = "Create anonymous functions concisely",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 134,
            language = ProgrammingLanguage.RUST,
            title = "Enums with Data",
            code = "enum Message {\n  Text(String),\n  Quit\n}",
            explanation = "Enums can hold different types of data",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 135,
            language = ProgrammingLanguage.RUST,
            title = "Ownership Transfer",
            code = "let s2 = s1; // s1 no longer valid",
            explanation = "Assignment moves ownership by default",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 136,
            language = ProgrammingLanguage.RUST,
            title = "Clone for Deep Copy",
            code = "let s2 = s1.clone()",
            explanation = "Explicitly clone to duplicate data",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 137,
            language = ProgrammingLanguage.RUST,
            title = "Println! Debug Format",
            code = "println!(\"{:?}\", my_struct)",
            explanation = "Use {:?} to print debug representation",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 138,
            language = ProgrammingLanguage.RUST,
            title = "Impl Blocks",
            code = "impl MyStruct {\n  fn new() -> Self { }\n}",
            explanation = "Define methods on structs with impl",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 139,
            language = ProgrammingLanguage.RUST,
            title = "Collect into Collections",
            code = "let v: Vec<_> = (0..10).collect()",
            explanation = "Convert iterators to collections with collect()",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 140,
            language = ProgrammingLanguage.RUST,
            title = "Turbofish Syntax",
            code = "let v = Vec::<i32>::new()",
            explanation = "Explicitly specify generic types with ::<>",
            category = SnippetCategory.SHORTCUT
        ),

        // Go snippets (141-160)
        CodeSnippet(
            id = 141,
            language = ProgrammingLanguage.GO,
            title = "Multiple Return Values",
            code = "result, err := doSomething()\nif err != nil {\n  return err\n}",
            explanation = "Go's idiomatic error handling pattern",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 142,
            language = ProgrammingLanguage.GO,
            title = "Defer for Cleanup",
            code = "defer file.Close()\n// file auto-closes on function exit",
            explanation = "Defer ensures cleanup happens even with early returns",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 143,
            language = ProgrammingLanguage.GO,
            title = "Short Variable Declaration",
            code = "name := \"John\"",
            explanation = "Use := for concise variable declaration with type inference",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 144,
            language = ProgrammingLanguage.GO,
            title = "Goroutines for Concurrency",
            code = "go processData()",
            explanation = "Launch concurrent functions with go keyword",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 145,
            language = ProgrammingLanguage.GO,
            title = "Channels for Communication",
            code = "ch := make(chan int)\nch <- 42\nval := <-ch",
            explanation = "Safely communicate between goroutines with channels",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 146,
            language = ProgrammingLanguage.GO,
            title = "Range Over Collections",
            code = "for i, v := range slice {\n  fmt.Println(i, v)\n}",
            explanation = "Iterate with index and value easily",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 147,
            language = ProgrammingLanguage.GO,
            title = "Blank Identifier",
            code = "_, err := doSomething()",
            explanation = "Use _ to ignore unwanted return values",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 148,
            language = ProgrammingLanguage.GO,
            title = "Init Function",
            code = "func init() {\n  // runs before main\n}",
            explanation = "Initialize package state automatically",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 149,
            language = ProgrammingLanguage.GO,
            title = "Struct Embedding",
            code = "type Manager struct {\n  Employee\n}",
            explanation = "Compose types instead of classical inheritance",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 150,
            language = ProgrammingLanguage.GO,
            title = "Interface Implementation",
            code = "// No explicit 'implements' needed\ntype MyType struct{}\nfunc (m MyType) Method() {}",
            explanation = "Interfaces are implemented implicitly",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 151,
            language = ProgrammingLanguage.GO,
            title = "Make vs New",
            code = "slice := make([]int, 10)\nptr := new(MyStruct)",
            explanation = "Make initializes slices/maps/channels, new returns pointer",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 152,
            language = ProgrammingLanguage.GO,
            title = "Variadic Functions",
            code = "func sum(nums ...int) int",
            explanation = "Accept variable number of arguments",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 153,
            language = ProgrammingLanguage.GO,
            title = "Select for Channel Multiplexing",
            code = "select {\ncase msg := <-ch1:\ncase msg := <-ch2:\n}",
            explanation = "Wait on multiple channel operations",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 154,
            language = ProgrammingLanguage.GO,
            title = "Append to Slices",
            code = "slice = append(slice, item)",
            explanation = "Must assign result back as underlying array may change",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 155,
            language = ProgrammingLanguage.GO,
            title = "Type Assertion",
            code = "str, ok := value.(string)",
            explanation = "Safely extract concrete type from interface",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 156,
            language = ProgrammingLanguage.GO,
            title = "Anonymous Structs",
            code = "person := struct {\n  name string\n}{name: \"John\"}",
            explanation = "Create one-off struct types inline",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 157,
            language = ProgrammingLanguage.GO,
            title = "Context for Cancellation",
            code = "ctx, cancel := context.WithTimeout(parent, 5*time.Second)\ndefer cancel()",
            explanation = "Pass cancellation signals across API boundaries",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 158,
            language = ProgrammingLanguage.GO,
            title = "Method Value Receivers",
            code = "func (p Person) String() string",
            explanation = "Methods with value receivers get a copy",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 159,
            language = ProgrammingLanguage.GO,
            title = "String Formatting",
            code = "msg := fmt.Sprintf(\"Hello %s, age %d\", name, age)",
            explanation = "Format strings similar to C's printf",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 160,
            language = ProgrammingLanguage.GO,
            title = "Empty Interface",
            code = "var data interface{} = \"anything\"",
            explanation = "interface{} can hold any type value",
            category = SnippetCategory.TRICK
        ),

        // Dart snippets (161-180)
        CodeSnippet(
            id = 161,
            language = ProgrammingLanguage.DART,
            title = "Null Safety Operator",
            code = "String? name = null;\nprint(name?.length)",
            explanation = "Use ? to safely access nullable properties",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 162,
            language = ProgrammingLanguage.DART,
            title = "Null Coalescing",
            code = "final name = username ?? 'Guest'",
            explanation = "Provide default value when null",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 163,
            language = ProgrammingLanguage.DART,
            title = "Late Variables",
            code = "late String description;",
            explanation = "Defer initialization of non-nullable variables",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 164,
            language = ProgrammingLanguage.DART,
            title = "Arrow Functions",
            code = "void greet() => print('Hello');",
            explanation = "Shorthand for single-expression functions",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 165,
            language = ProgrammingLanguage.DART,
            title = "Collection If",
            code = "var items = ['a', if (isValid) 'b']",
            explanation = "Conditionally include elements in collections",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 166,
            language = ProgrammingLanguage.DART,
            title = "Collection For",
            code = "var list = [for (var i in items) i * 2]",
            explanation = "Use for loops inside collection literals",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 167,
            language = ProgrammingLanguage.DART,
            title = "Spread Operator",
            code = "var combined = [...list1, ...list2]",
            explanation = "Merge collections with spread syntax",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 168,
            language = ProgrammingLanguage.DART,
            title = "Named Constructors",
            code = "class Point {\n  Point.origin() : x = 0, y = 0;\n}",
            explanation = "Create multiple constructors with descriptive names",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 169,
            language = ProgrammingLanguage.DART,
            title = "Cascade Notation",
            code = "var paint = Paint()\n  ..color = Colors.blue\n  ..strokeWidth = 5.0",
            explanation = "Chain operations on same object with ..",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 170,
            language = ProgrammingLanguage.DART,
            title = "Extension Methods",
            code = "extension on String {\n  bool get isEmail => contains('@');\n}",
            explanation = "Add methods to existing classes",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 171,
            language = ProgrammingLanguage.DART,
            title = "Async/Await",
            code = "Future<String> fetchData() async {\n  return await getData();\n}",
            explanation = "Handle asynchronous operations cleanly",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 172,
            language = ProgrammingLanguage.DART,
            title = "Factory Constructors",
            code = "factory Logger(String name) {\n  return _cache[name] ??= Logger._internal(name);\n}",
            explanation = "Control instance creation with factory pattern",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 173,
            language = ProgrammingLanguage.DART,
            title = "Const Constructors",
            code = "const point = Point(0, 0)",
            explanation = "Create compile-time constants for better performance",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 174,
            language = ProgrammingLanguage.DART,
            title = "Required Named Parameters",
            code = "void login({required String username})",
            explanation = "Make named parameters mandatory",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 175,
            language = ProgrammingLanguage.DART,
            title = "Type Inference with var",
            code = "var name = 'John'; // String inferred",
            explanation = "Let Dart infer types automatically",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 176,
            language = ProgrammingLanguage.DART,
            title = "Null Assertion Operator",
            code = "String name = nullableName!",
            explanation = "Assert non-null, but can throw at runtime",
            category = SnippetCategory.GOTCHA
        ),
        CodeSnippet(
            id = 177,
            language = ProgrammingLanguage.DART,
            title = "Records (Dart 3)",
            code = "var record = ('John', 25);\nvar (name, age) = record;",
            explanation = "Anonymous composite types for grouping values",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 178,
            language = ProgrammingLanguage.DART,
            title = "Pattern Matching (Dart 3)",
            code = "switch (value) {\n  case int i => print('Int: \$i');\n  case String s => print('String: \$s');\n}",
            explanation = "Powerful pattern matching in switch statements",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 179,
            language = ProgrammingLanguage.DART,
            title = "Getters and Setters",
            code = "String get fullName => '\$first \$last';\nset fullName(String name) => ...",
            explanation = "Define computed properties with get and set",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 180,
            language = ProgrammingLanguage.DART,
            title = "Final vs Const",
            code = "final time = DateTime.now(); // Runtime\nconst pi = 3.14; // Compile-time",
            explanation = "Final is runtime constant, const is compile-time",
            category = SnippetCategory.GOTCHA
        )
    )

    fun getRandomSnippet(): CodeSnippet {
        return snippets.random()
    }

    fun getRandomSnippetByLanguage(languageName: String?): CodeSnippet {
        val filteredSnippets = if (languageName != null) {
            snippets.filter { it.language.name == languageName }
        } else {
            snippets
        }
        return filteredSnippets.randomOrNull() ?: snippets.random()
    }

    fun getSnippetsByLanguage(language: ProgrammingLanguage): List<CodeSnippet> {
        return snippets.filter { it.language == language }
    }

    fun getSnippetById(id: Int): CodeSnippet? {
        return snippets.find { it.id == id }
    }

    fun getSnippetByIdAndLanguage(id: Int, languageName: String?): CodeSnippet? {
        val filteredSnippets = if (languageName != null) {
            snippets.filter { it.language.name == languageName }
        } else {
            snippets
        }
        return filteredSnippets.find { it.id == id }
    }

    fun getAllSnippets(): List<CodeSnippet> {
        return snippets
    }

    fun getNextSnippet(currentId: Int, languageName: String? = null): CodeSnippet {
        android.util.Log.d("SnippetRepository", "getNextSnippet called with languageName: $languageName")

        val filteredSnippets = if (languageName != null) {
            val filtered = snippets.filter { it.language.name == languageName }
            android.util.Log.d("SnippetRepository", "Filtered ${filtered.size} snippets for language: $languageName")
            filtered
        } else {
            android.util.Log.d("SnippetRepository", "No filter - using all ${snippets.size} snippets")
            snippets
        }

        if (filteredSnippets.isEmpty()) {
            android.util.Log.w("SnippetRepository", "No snippets found for language: $languageName, falling back to all")
            return snippets.random()
        }

        val currentIndex = filteredSnippets.indexOfFirst { it.id == currentId }
        if (currentIndex == -1) {
            // Current snippet not in filtered list, return first filtered snippet
            android.util.Log.d("SnippetRepository", "Current ID $currentId not in filtered list, returning first")
            return filteredSnippets.firstOrNull() ?: snippets.first()
        }

        val nextIndex = (currentIndex + 1) % filteredSnippets.size
        val nextSnippet = filteredSnippets[nextIndex]
        android.util.Log.d("SnippetRepository", "Returning snippet ${nextSnippet.id}: ${nextSnippet.title} (${nextSnippet.language.name})")
        return nextSnippet
    }

    // Get next snippet from multiple selected languages
    fun getNextSnippetFromLanguages(currentId: Int, languageNames: Set<String>): CodeSnippet {
        android.util.Log.d("SnippetRepository", "getNextSnippetFromLanguages called with languages: $languageNames")

        val filteredSnippets = if (languageNames.isNotEmpty()) {
            val filtered = snippets.filter { it.language.name in languageNames }
            android.util.Log.d("SnippetRepository", "Filtered ${filtered.size} snippets for languages: $languageNames")
            filtered
        } else {
            android.util.Log.d("SnippetRepository", "No filter - using all ${snippets.size} snippets")
            snippets
        }

        if (filteredSnippets.isEmpty()) {
            android.util.Log.w("SnippetRepository", "No snippets found for languages: $languageNames, falling back to all")
            return snippets.random()
        }

        // Return random snippet from filtered list
        return filteredSnippets.random()
    }
}
