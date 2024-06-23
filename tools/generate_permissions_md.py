#  GPLv3 License
#
#  Copyright (c) 2024-2024 JVerbruggen
#  https://github.com/JVerbruggen/jrides
#
#  This software is protected under the GPLv3 license,
#  that can be found in the project's LICENSE file.
#
#  In short, permission is hereby granted that anyone can copy, modify and distribute this software.
#  You have to include the license and copyright notice with each and every distribution. You can use
#  this software privately or commercially. Modifications to the code have to be indicated, and
#  distributions of this code must be distributed with the same license, GPLv3. The software is provided
#  without warranty. The software author or license can not be held liable for any damages
#  inflicted by the software.

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
        
