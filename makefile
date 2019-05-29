# javac -d . src/*.java
JFLAGS = -g -d
JC = javac

SRC_DIR = src
BUILD_DIR = build/bubblegame

.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $(BUILD_DIR) $(SRC_DIR)/*.java

CLASSES = \
        $(SRC_DIR)/Index.java \
        $(SRC_DIR)/BubbleGame.java \

default: classes

classes: \
		$(BUILD_DIR)/Index.class \
		$(BUILD_DIR)/BubbleGame.class

clean:
		$(RM) $(BUILD_DIR)/*.class