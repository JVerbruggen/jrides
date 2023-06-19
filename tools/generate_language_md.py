import re

java_file_path = "src/main/java/com/jverbruggen/jrides/language/LanguageFile.java"
expression = r"\);*\n?\s+setLanguageDefault\(LanguageFileField."
language_file_path = "docs/language.md"

java_tags_file_path = "src/main/java/com/jverbruggen/jrides/language/LanguageFileTag.java"
tags_expression = r"{{1}\n\s+public\sstatic\sfinal\sString\s|public\sstatic\sfinal\sString\s|;\n\s+|;\n"

tag_replacement_base_expression = r"\"\s\+\sLanguageFileTag.{java_name}\s\+\s\""

content = """
# Language

Key | Default value
--- | ---
"""

tags = []

with open(java_tags_file_path) as f:
    file_content = f.read()
    split = re.split(tags_expression, file_content)[2:-1]
    
    for item in split:
        if item == "": continue
        item_split = item.split(" = ")
        java_name = item_split[0]
        replace_name = item_split[1].replace("\"", "")
        res = (java_name, replace_name)
        tags += [res]

grouped = []
with open(java_file_path) as f:
    file_content = f.read()
    split = re.split(expression, file_content)[1:-1]
    
    for item in split:
        t = tuple(item.split(sep=', ', maxsplit=1))
        
        value = t[1]
        
        for (java_name, replace_name) in tags:
            tag_replacement_expression = tag_replacement_base_expression.format(java_name=java_name)
            value = re.sub(tag_replacement_expression, replace_name, value)
        
        content += f"{t[0]} | {value}\n"
        
with open(language_file_path, 'w') as f:
    f.write(content)
    
print("Done!")
        
