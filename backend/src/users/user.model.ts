import { Column, Model, Table } from 'sequelize-typescript';

@Table({ timestamps: true })
export class User extends Model {
  @Column({ primaryKey: true })
  username: string;

  @Column
  password: string;
}
