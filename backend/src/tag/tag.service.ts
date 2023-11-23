import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { Tag } from './tag.model';

@Injectable()
export class TagService {
  constructor(@InjectModel(Tag) private tagModel: typeof Tag) {}

  async createOrFind(tags: string[]) {
    const promises = tags.map(async (t) => {
      const o = await this.tagModel.findByPk(t);

      if (o !== null) {
        return o;
      }

      return await this.tagModel.create({ name: t });
    });

    return Promise.all(promises);
  }
}
