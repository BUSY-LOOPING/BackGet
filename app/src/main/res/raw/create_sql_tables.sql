CREATE TABLE uri_model_table (`primary_key` TEXT PRIMARY KEY NOT NULL, `regular` TEXT NOT NULL, `full` TEXT NOT NULL, `raw` TEXT NOT NULL);;;

CREATE TABLE user_model_table (`id` TEXT PRIMARY KEY NOT NULL, `name` TEXT NOT NULL, `bio` TEXT, `username` TEXT NOT NULL,
                            FOREIGN KEY (`id`) REFERENCES user_profile_image_table(`id`) ON DELETE CASCADE);;;

CREATE TABLE liked_table (`image_id` TEXT PRIMARY KEY, `description` TEXT, `user_model` TEXT NOT NULL, `likes` TEXT NOT NULL,
                         FOREIGN KEY (`image_id`) REFERENCES uri_model_table(`primary_key`) ON DELETE CASCADE,
                          FOREIGN KEY (`user_model`) REFERENCES user_model_table(`id`) ON DELETE CASCADE);;;

CREATE TABLE user_profile_image_table (`id` VARCHAR(20) PRIMARY KEY NOT NULL, `small` TEXT NOT NULL, `medium` TEXT NOT NULL, `large` TEXT NOT NULL) ;;;