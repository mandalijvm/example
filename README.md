
**Mandali** is an innovative library crafted to proactively detect and address potential thread-safety issues in Java and Kotlin classes.

With **Mandali**, we can effortlessly identify non-thread-safe data structures that may jeopardize your application's stability in multi-threaded environments. Beyond that, it offers seamless deadlock detection, ensuring smooth and reliable performance during runtime. Elevate your code's safety and reliability with Mandali’s smart, automated solutions.

## Installation
### In Gradle

```groovy
repositories {
    maven { url 'https://repo.repsy.io/mvn/hangga/repo' }
}

dependencies {
    implementation 'io.mandali:mandali:1.0.10-SNAPSHOT'
}
```
### In Maven
```xml
<repositories>
    <repository>
        <id>mandali-repo</id>
        <url>https://repo.repsy.io/mvn/hangga/repo</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.mandali</groupId>
        <artifactId>mandali</artifactId>
        <version>1.0.10-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Basic Usage
To start the Mandali analysis, simply create an instance of Mandali and call `start()` on it:
```Kotlin
Mandali(this).start()
```

## Annotation Usage: @RunMandali
This annotation should be applied at the class level of a test to trigger the analysis based on the specified options:


| Param          |descriptions| type              | default |
|:---------------|:---------------|:------------------|:--------|
| showDate       | A Boolean parameter that, when set to true, displays the date and time of the analysis run in the output.| boolean | true    |
| detectDeadlock | A Boolean parameter that enables deadlock detection|boolean   | false    |


Yes, just this simple approach:

```Kotlin
@RunMandali(showDate = true, detectDeadlock = true)
class MandaliExampleUnitTest {
    @Test
    fun `some test example`() {
        //...

        Mandali(this).start()
    }
}
```

## Attention, please

It is important to note that this library is very useful for the development stage. But because Mandali works by checking each line of code, it is **not recommended for the production stage**.

## Example Test Class
The following example demonstrates how to structure a test class to check for thread safety issues using the Mandali library. This class simulates potential deadlocks and thread-unsafe scenarios, which are detected and analyzed by Mandali.

```Kotlin
package io.mandali

import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class Account(val name: String, var balance: Int) {

    private fun deposit(amount: Int) {
        balance += amount
    }

    private fun withdraw(amount: Int) {
        balance -= amount
    }

    fun transfer(to: Account, amount: Int) {
        println("${this.name} tries to transfer $amount to ${to.name}.")
        synchronized(this) {
            sleep(10) // Simulate processing time
            if (balance >= amount) {
                withdraw(amount)
                synchronized(to) {
                    to.deposit(amount)
                }
            }
        }
    }
}

@RunMandali(showDate = true, detectDeadlock = true)
class MandaliExampleKotlinUnitTest {

    @Test
    fun `example of deadlock`() {
        val account1 = Account("Hangga", 1000)
        val account2 = Account("John", 1000)
        val account3 = Account("Alice", 2000)

        // Transfer from account1 to account2
        thread {
            account1.transfer(account2, 100)
        }.join(10) // as a simulation of the time required

        // Transfer from account2 to account1
        thread {
            account2.transfer(account1, 200)
        }.join(20)

        // Transfer from account3 to account1
        thread {
            account3.transfer(account1, 1000)
        }.join(500)

        Mandali(this).start()
    }

    val list = ArrayList()

    @Test
    fun `example thread-unsafe using HashMap`() {
        val map = HashMap()

        val threads = List(10) { index ->
            thread {
                for (i in 0 until 1000) {
                    map[i] = index
                }
            }
        }

        threads.forEach {
            it.join()
        }
    }
}
```

## About

The name **Mandali** is inspired by the Indonesian words "Man = Aman" (safe) and "Dali = terkenDali" (controlled), symbolizing **safety under control**.

## License

Mandali is a proprietary, closed-source library with restrictions on redistribution and modification.

It is free to use, but if you find it valuable, you are welcome to make a donation or become a sponsor. For more information, please contact our development team.