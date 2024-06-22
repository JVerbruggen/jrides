import re

java_file_path = "src/main/java/com/jverbruggen/jrides/common/permissions/Permissions.java"
permission_groups_re = r'\s+\/\/\/\s|}'
permission_split_re = r'\s=\s\"|\";\s\/\/\s'
language_file_path = "docs/permissions.md"

table_template = "Permission | Description\n--- | ---"
content = "# Permissions\n"

grouped = []
with open(java_file_path) as f:
    file_content = f.read()
    permission_groups = re.split(permission_groups_re, file_content)[1:-1]
    
    for p_group in permission_groups:
        values = re.split(r'\n\s+', str(p_group))
        title = values[0]
        permissions = values[1::]
        
        permissions_tuples = [tuple(re.split(permission_split_re, permission)[1:]) for permission in permissions]
        table_contents = '\n'.join([f"{permission} | {description}" for permission, description in permissions_tuples])
        
        content += f"\n## {title}\n\n{table_template}\n{table_contents}\n"
        
with open(language_file_path, 'w') as f:
    f.write(content)
    
print("Done!")
        
