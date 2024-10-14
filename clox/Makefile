# Makefile

# Compiler
CC = clang

# Compiler flags
CFLAGS = -Wall -Wextra -std=c11 -I$(INCDIR)

# Directories
SRCDIR = src
INCDIR = include
BINDIR = bin

# Target executable
TARGET = $(BINDIR)/list_ops

# Source files
SRCS = $(SRCDIR)/double_linked_list.c $(SRCDIR)/list_ops.c

# Object files
OBJS = $(SRCS:$(SRCDIR)/%.c=$(BINDIR)/%.o)

# Header files
HEADERS = $(INCDIR)/double_linked_list.h

# Default target
all: $(TARGET)

# Rule to build the target executable
$(TARGET): $(OBJS) | $(BINDIR)
	$(CC) $(CFLAGS) -o $(TARGET) $(OBJS)

# Rule to build object files
$(BINDIR)/%.o: $(SRCDIR)/%.c $(HEADERS) | $(BINDIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(BINDIR):
	mkdir -p $(BINDIR)

# Clean up build files
clean:
	rm -rf $(BINDIR)

# Phony targets
.PHONY: all clean
