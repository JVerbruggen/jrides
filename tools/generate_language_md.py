import re

java_file_path = "src/main/java/com/jverbruggen/jrides/language/LanguageFile.java"
expression = r"\);*\n?\s+setLanguageDefault\(LanguageFileFields."
language_file_path = "docs/language.md"

content = """
# Language

Key | Default value
--- | ---
"""

grouped = []
with open(java_file_path) as f:
    file_content = f.read()
    split = re.split(expression, file_content)[1:-1]
    
    for item in split:
        t = tuple(item.split(sep=', ', maxsplit=1))
        content += f"{t[0]} | {t[1]}\n"
        
with open(language_file_path, 'w') as f:
    f.write(content)
    
print("Done!")
        
