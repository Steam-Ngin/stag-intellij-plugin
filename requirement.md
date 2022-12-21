The PyCharm Gradle plugin will have two features:

### Feature #1 (New Project Wizard of type "STAG")

"New Project wizard will include an option called "STAG". Following are the parameters on the wizard

__Parameter #1 (STAG Name)__

This is the STAG name. It must end in the word "stag". e.g. petstag, demostag, googlestag etc.. Set default value to "petstag".

__Parameter #2 (STAG pattern)__

This is a dropdown menu. Contains one values ==> `rubicon`. Of course, this list will grow in future.

__Parameter #3 (STAG pattern version)__

This is a dropdown menu. Contains one values ==> `1.0.0-alpha`. Of course, this list will grow in future.

__Parameter #4 (STAG Location)__

This is basically the project directory.
Default value is `$USER_HOME/steamworks/stem/<stag_pattern>/<stag_version>/<stag_name>`

E.g. (on windows) ==> `C:\Users\TestUser\steamworks\stem\rubicon\1.0.0\petstag`

__Parameter #5 (Override Config)__

This is a boolean flag. So, basically a check box in the wizard.

Once you have the above 5 parameters captured from the user, you call the underlying command line utility called steamctl (this utility will be provided to you by me). For .e.g

```
steamctl desktop create stag --name <stag name> --pattern <stag pattern> --version <stag pattern version> --stag-dir <stag location> --override-cfg
```

### Feature #2 ("Deploy STAG" button somewhere)

This assumes you are already inside the newly created STAG project. 

So, you must already have the following parameters available: 1. STAG name, 2. STAG pattern, 3. STAG pattern version ==> that you captured from the above wizard.

Now, I need a button or some widget somewhere in the IDE that says "Deploy STAG". When the button/widget is clicked, it should pop up a small window with the following two params:

__Parameter #1 (Emulate Custer)__

This is a boolean flag. So, it will be a checkbox.

__Parameter #2 (Deployment Config)__

This is the path to the deployment config file.

Default value is `$USER_HOME/steamworks/steamctlcfg/<stag_pattern>/<stag_pattern_version>/<stag_ name>steamctlcfg/desktop_deploment_config.yml`

E.g. On windows ==> `c:\users\testuser\steamworks\steamctlcfg\rubicon\1.0.0\petstagsteamctlcfg\desktop_deploment_config.yml`

Once you have captured the above two parameters and user clicks on "Deploy" button, then call the underlying command line utility. For e.g.

`steamctl desktop deploy stag --name <stag_name> --pattern <stag_pattern> --version <stag_pattern_version> --emulate-cluster`
