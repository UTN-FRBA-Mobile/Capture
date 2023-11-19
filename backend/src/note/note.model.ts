import {
  Table,
  Model,
  Column,
  BelongsTo,
  AllowNull,
  HasMany,
} from 'sequelize-typescript';
import { User } from '../users/user.model';
import { Tag } from '../tag/tag.model'; // Import the Tag model

@Table({ timestamps: true })
export class Note extends Model {
  @BelongsTo(() => User, 'username')
  user: User;

  @Column
  title: string;

  @Column
  content: string;

  @AllowNull
  @Column
  imageName: string;

  @AllowNull
  @Column
  audioName: string;

  @HasMany(() => Tag) // Use HasMany to represent one-to-many relationship
  tags: Tag[]; // This array will contain multiple Tag instances associated with the Note

  // ... other fields and associations
}
