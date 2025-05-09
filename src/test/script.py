import os

# Folder yang mau di-skip
SKIP_DIRS = {'culik', '/.gitignore', '/.gitattributes', 'build', '.gradle', '.idea', '.github', '.git', 'build', 'gradle', 'script.py'}

def generate_structure_and_content(base_path):
    structure = []
    content = []

    for root, dirs, files in os.walk(base_path):
        # Filter out dirs we want to skip
        dirs[:] = [d for d in dirs if d not in SKIP_DIRS]

        level = root.replace(base_path, '').count(os.sep)
        indent = '  ' * level
        folder_name = os.path.basename(root) if root != base_path else os.path.basename(base_path)
        structure.append(f"{indent}{folder_name}/")

        sub_indent = '  ' * (level + 1)
        for f in files:
            structure.append(f"{sub_indent}{f}")
            file_path = os.path.join(root, f)
            relative_path = os.path.relpath(file_path, base_path)
            try:
                with open(file_path, 'r', encoding='utf-8') as file_content:
                    content.append(f"\n/{relative_path}\n{'-' * 40}\n{file_content.read()}")
            except Exception as e:
                content.append(f"\n/{relative_path}\n{'-' * 40}\nError reading file: {e}")

    return "\n".join(structure), "\n".join(content)

if __name__ == "__main__":
    base_path = os.getcwd()  # jalankan dari folder project
    structure, content = generate_structure_and_content(base_path)

    # Print hasilnya
    print("STRUKTUR DIREKTORI\n" + "="*20)
    print(structure)

    print("\n\nDETAIL FILE\n" + "="*20)
    print(content)